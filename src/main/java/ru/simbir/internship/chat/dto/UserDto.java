package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserStatus;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String login;
    private String password;
    private UserStatus status;
    private UserAppRole userAppRole;
    private Date created;
    private Date updated;
}
