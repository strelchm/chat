package ru.simbir.internship.chat.service;

import org.springframework.security.access.annotation.Secured;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserStatus;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(UUID id);
    UUID add(UserDto dto);
    UserDto edit(UserDto dto, UserDto userDto);
    void delete(UUID id, UserDto userDto);
    User getUserById(UUID id);
    void blockUser(UUID userId, UserDto userDto);
    void unblockUser(UUID userId, UserDto userDto);
}
