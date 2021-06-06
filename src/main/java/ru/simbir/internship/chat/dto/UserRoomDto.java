package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simbir.internship.chat.domain.UserRoomRole;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoomDto {
    private UserDto user;
    private UserRoomRole userRoomRole;
    private LocalDateTime blockedTime;
    private Date created;
    private Date updated;
}
