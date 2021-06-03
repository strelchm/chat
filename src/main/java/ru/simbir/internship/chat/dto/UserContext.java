package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.simbir.internship.chat.domain.User;


import java.util.Optional;

@Data
@AllArgsConstructor
public class UserContext {
    private Optional<UserDto> user;
}
