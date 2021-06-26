package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.repository.UserRoomRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class CheckRoomAccessService {
    protected final UserRoomRepository userRoomRepository;

    public CheckRoomAccessService(UserRoomRepository userRoomRepository) {
        this.userRoomRepository = userRoomRepository;
    }

    protected UserRoom getUserRoomByUserAndRoom(UUID userId, UUID roomId) {
        return userRoomRepository.findByUser_IdAndRoom_Id(userId, roomId).orElseThrow(
                () -> new AccessDeniedException("User " + userId + " is not a member of chat room " + roomId)
        );
    }

    protected UserRoom checkRoomAccess(UserDto userDto, UUID roomId, UserRoomRole... accessDeniedRoles) {
        if (userDto.getUserAppRole() == UserAppRole.ADMIN) {
            return null;
        }

        UserRoom userRoom = getUserRoomByUserAndRoom(userDto.getId(), roomId);
        if (accessDeniedRoles != null && accessDeniedRoles.length != 0 && Arrays.stream(accessDeniedRoles).anyMatch(gr -> gr == userRoom.getUserRoomRole())) {
            if (userRoom.getUserRoomRole().equals(UserRoomRole.BLOCKED_USER)
                    && userRoom.getBlockedTime().isBefore(LocalDateTime.now())) {
                System.out.println(userRoom.getBlockedTime());
                System.out.println(LocalDateTime.now());
                userRoom.setUserRoomRole(UserRoomRole.USER);
                userRoom.setBlockedTime(null);
                userRoomRepository.save(userRoom);
                return checkRoomAccess(userDto, roomId, accessDeniedRoles);
            }
            throw new AccessDeniedException("Permission denied for user " + userDto.getId() + " with role " +
                    userRoom.getUserRoomRole() + " in chat room " + roomId);
        }
        return userRoom;
    }
}
