package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface WebSocketService {
    MessageDto process(MessageDto dto, UUID roomID, UserDto userDto);

    List<MessageDto> processBot(MessageDto dto, UserDto userDto);
}