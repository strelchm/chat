package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.RoomRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.service.CheckRoomAccessService;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends CheckRoomAccessService implements RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;
    private static final Logger log = Logger.getLogger(RoomServiceImpl.class.getName());

    @Autowired
    public RoomServiceImpl(RoomRepository repository, UserService userService, UserRoomRepository userRoomRepository) {
        super(userRoomRepository);
        this.userService = userService;
        this.roomRepository = repository;
    }

    @Override
    public List<RoomDto> getAll(UserDto userDto) {
        return roomRepository.findAll().stream().map(MappingUtil::mapToRoomDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> getAllByUser(UserDto userDto) {
        List<UserRoom> userRooms = userRoomRepository.findByUser_Id(userDto.getId());

        if (userRooms == null) {
            throw new BadRequestException("Chat rooms not found for user with id " + userDto.getId());
        }

        return userRooms.stream().map(UserRoom::getRoom).map(MappingUtil::mapToRoomDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto getById(UUID id, UserDto userDto) { // ???????????????? ???????????????? ???????????? ???????? ?????????????????????? ????????????, ???? ?????????????????? - ???????????? ?????? ????????????????????
        return MappingUtil.mapToRoomDto(getRoomById(id));
    }

    @Override
    public UUID add(RoomDto dto, UserDto userDto) {
        Room room = roomRepository.save(MappingUtil.mapToRoomEntity(dto));

        UserRoom userRoom = new UserRoom();

        UserRoom.Key key = new UserRoom.Key();
        key.setRoomId(room.getId());
        key.setUserId(userDto.getId());

        userRoom.setId(key);
        userRoom.setRoom(room);
        userRoom.setUser(userService.getUserById(userDto.getId()));
        userRoom.setUserRoomRole(UserRoomRole.OWNER);
        userRoomRepository.save(userRoom);

        return room.getId();
    }

    @Override
    public RoomDto edit(RoomDto dto, UserDto userDto) {
        checkRoomAccess(userDto, dto.getId(), UserRoomRole.MODERATOR, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);

        Room room = getRoomById(dto.getId());
        if (!dto.getName().equals(dto.getName())) {
            room.setName(dto.getName());
        }

        if (!dto.getType().equals(dto.getType())) {
            room.setType(dto.getType());
        }
        return MappingUtil.mapToRoomDto(roomRepository.save(room));
    }

    @Override
    public void addMembers(UUID roomId, UserDto userDto, UUID... idArray) {
        checkRoomAccess(userDto, roomId, UserRoomRole.BLOCKED_USER);

        for (UUID id : idArray) {
            try {
                getUserRoomByUserAndRoom(id, roomId);
                log.warning("User with id " + id + " already exists in chat room " + roomId);
                continue;
            } catch (AccessDeniedException ex) {
                UserRoom userRoom = new UserRoom();

                UserRoom.Key key = new UserRoom.Key();
                key.setRoomId(roomId);
                key.setUserId(id);
                userRoom.setId(key);
                userRoom.setRoom(getRoomById(roomId));
                userRoom.setUser(userService.getUserById(id));
                userRoom.setUserRoomRole(UserRoomRole.USER);
                userRoomRepository.save(userRoom);
            }
        }
    }

    @Override
    public void removeMembers(UUID roomId, UserDto userDto, UUID... idArray) {
        checkRoomAccess(userDto, roomId, UserRoomRole.USER, UserRoomRole.MODERATOR, UserRoomRole.BLOCKED_USER);
        for (UUID id : idArray) {
            userRoomRepository.delete(getUserRoomByUserAndRoom(id, roomId));
        }
    }


    @Override
    @Secured("ROLE_ADMIN")
    public void moderatorAdd(UUID roomId, UserDto userDto, UUID moderatorId) {
        try {
            UserRoom userRoom = getUserRoomByUserAndRoom(moderatorId, roomId);
            userRoom.setUserRoomRole(UserRoomRole.MODERATOR);
            userRoomRepository.save(userRoom);
        } catch (AccessDeniedException ex) {
            addMembers(roomId, userDto, moderatorId);
            moderatorAdd(roomId, userDto, moderatorId);
        }
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void moderatorRemove(UUID roomId, UserDto userDto, UUID moderatorId) {
        UserRoom userRoom = getUserRoomByUserAndRoom(moderatorId, roomId);
        if (userRoom.getUserRoomRole() != UserRoomRole.MODERATOR) {
            throw new BadRequestException("User is not moderator");
        }
        userRoomRepository.delete(userRoom);
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        Room room = getRoomById(id);
        checkRoomAccess(userDto, id, UserRoomRole.MODERATOR, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        roomRepository.delete(room);
    }

    @Override
    public Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room with id " + id + " not found"));
    }

    @Override
    public Room getRoomByName(String name) {
        return roomRepository.findByName(name).orElseThrow(() -> new NotFoundException("Room with name " + name + " not found"));
    }
}
