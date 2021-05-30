package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> getAll();
    UserDto getById(UUID id);
    UUID add(UserDto dto);
    UserDto edit(UserDto dto);
    void delete(UUID id);

}
