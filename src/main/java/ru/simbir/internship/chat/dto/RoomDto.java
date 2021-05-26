package ru.simbir.internship.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simbir.internship.chat.domain.RoomType;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private UUID id;
    private String name;
    private Set<MessageDto> messages;
    private Set<UserDto> users;
    private RoomType type;
    private Date created;
    private Date updated;

}
