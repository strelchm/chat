package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.*;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.RoomRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.service.CheckRoomAccessService;
import ru.simbir.internship.chat.service.UserRoomService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class UserRoomImpl extends CheckRoomAccessService implements UserRoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public UserRoomImpl(RoomRepository roomRepository, UserRoomRepository userRoomRepository) {
        super(userRoomRepository);
        this.roomRepository= roomRepository;
    }

    @Override
    public void deregisterUser(User user, UUID roomId, UserDto userDto) {
        if (userDto.getId() != user.getId()) {
            checkRoomAccess(userDto, roomId, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        }
        UserRoom userRoom = userRoomRepository.findByUser_IdAndRoom_Id(user.getId(), roomId)
                .orElseThrow(NotFoundException::new);
        if (userRoom.getUserRoomRole().equals(UserRoomRole.OWNER)) {
            throw new BadRequestException("Not applicable to room owner.");
        }
        userRoomRepository.delete(userRoom);
    }

    @Override
    public void banUser(User user, UUID roomId, int minutes, UserDto userDto) {
        if (userDto.getId() != user.getId()) {
            checkRoomAccess(userDto, roomId, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        }
        UserRoom userRoom = userRoomRepository.findByUser_IdAndRoom_Id(user.getId(), roomId)
                .orElseThrow(NotFoundException::new);
        if (userRoom.getUserRoomRole().equals(UserRoomRole.OWNER)) {
            throw new BadRequestException("Not applicable to room owner.");
        }
        userRoom.setUserRoomRole(UserRoomRole.BLOCKED_USER);
        LocalDateTime blockedTime = LocalDateTime.now();
        blockedTime = blockedTime.plus(minutes, ChronoUnit.MINUTES);
        userRoom.setBlockedTime(blockedTime);
        userRoomRepository.save(userRoom);
    }


    @Override
    public void registerUser(User user, UUID roomId, UserRoomRole role, UserDto userDto) {
        UserRoom userRoom = userRoomRepository.findByUser_IdAndRoom_Id(user.getId(), roomId).orElse(new UserRoom());
        if (userRoom.getUserRoomRole() != null && userRoom.getUserRoomRole().equals(UserRoomRole.OWNER)) {
            throw new BadRequestException("Not applicable to room owner.");
        }
        Room room = roomRepository.getById(roomId);
        if (room.getType().equals(RoomType.PRIVATE)) {
            checkRoomAccess(userDto, roomId, UserRoomRole.USER, UserRoomRole.BLOCKED_USER);
        }
        userRoom.setRoom(room);
        userRoom.setUser(user);
        UserRoom.Key key = new UserRoom.Key();
        key.setRoomId(roomId);
        key.setUserId(user.getId());
        userRoom.setId(key);
        userRoom.setCreated(new Date());
        userRoom.setUserRoomRole(role);
        userRoomRepository.save(userRoom);
    }

}
