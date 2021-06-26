package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.UUID;

public interface UserRoomService {
    void registerUser(User user, UUID roomId, UserRoomRole role, UserDto userDto);
    void deregisterUser(User user, UUID roomId, UserDto userDto);
    void banUser(User user, UUID roomId, int minutes, UserDto userDto);
}
