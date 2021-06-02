package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDto> getAll(UUID chatRoomId, UserDto userDto);

    MessageDto getById(UUID messageId, UUID chatRoomId, UserDto userDto);

    UUID add(MessageDto dto, UUID chatRoomId, UserDto userDto);

    MessageDto edit(MessageDto dto, UUID chatRoomId, UserDto userDto);

    void delete(UUID messageId, UUID chatRoomId, UserDto userDto);
}
