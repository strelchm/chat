package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.User;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private String text;
    private UserDto user;
    private RoomDto room;
    private Date created;
    private Date updated;
}