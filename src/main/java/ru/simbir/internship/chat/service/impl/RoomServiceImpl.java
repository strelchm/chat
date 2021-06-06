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
    private final UserRoomRepository userRoomRepository;
    private final UserService userService;
    private static final Logger log = Logger.getLogger(RoomServiceImpl.class.getName());

    @Autowired
    public RoomServiceImpl(RoomRepository repository, UserService userService, UserRoomRepository userRoomRepository) {
        super(userRoomRepository);
        this.userService = userService;
        this.roomRepository = repository;
        this.userRoomRepository = userRoomRepository;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<RoomDto> getAllForAdmin(UserDto userDto) { // todo разделить с методом ниже, дабы закрыть секьюрной аннотацией
        checkAccess(userDto, null);
        return roomRepository.findAll().stream().map(MappingUtil::mapToRoomDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> getAllForUser(UserDto userDto) {
        List<UserRoom> userRooms = userRoomRepository.findByUser_Id(userDto.getId());

        if (userRooms == null) {
            throw new BadRequestException("Chat rooms not found for user with id " + userDto.getId());
        }

        return userRooms.stream().map(UserRoom::getRoom).map(MappingUtil::mapToRoomDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto getById(UUID id, UserDto userDto) { // смотреть описание любого чата допускается любому, но сообщения - только его участникам
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
        checkAccess(userDto, dto.getId(), UserRoomRole.MODERATOR, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        getRoomById(dto.getId());
        roomRepository.save(MappingUtil.mapToRoomEntity(dto));
        return dto;
    }

    @Override
    public void addMembers(UUID roomId, UserDto userDto, UUID... idArray) {
        checkAccess(userDto, roomId, UserRoomRole.BLOCKED_USER);

        for (UUID id : idArray) {
            try {
                getUserRoomByUserAndRoom(userDto.getId(), id);
                log.warning("User with id " + userDto.getId() + " already exists in chat room " + roomId);
                continue;
            } catch (AccessDeniedException ex) {
                UserRoom userRoom = new UserRoom();

                UserRoom.Key key = new UserRoom.Key();
                key.setRoomId(roomId);
                key.setUserId(userDto.getId());

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
        checkAccess(userDto, roomId, UserRoomRole.USER, UserRoomRole.MODERATOR, UserRoomRole.BLOCKED_USER);
        for (UUID id : idArray) {
            userRoomRepository.delete(getUserRoomByUserAndRoom(userDto.getId(), id));
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
        }
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void moderatorRemove(UUID roomId, UserDto userDto, UUID moderatorId) {
        UserRoom userRoom = getUserRoomByUserAndRoom(moderatorId, roomId);
        if(userRoom.getUserRoomRole() != UserRoomRole.MODERATOR) {
            throw new BadRequestException("User is not moderator");
        }
        userRoomRepository.delete(userRoom);
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        getRoomById(id);
        checkAccess(userDto, id, UserRoomRole.MODERATOR, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        roomRepository.deleteById(id);
    }

    private Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room with id " + id + " not found"));
    }
}
