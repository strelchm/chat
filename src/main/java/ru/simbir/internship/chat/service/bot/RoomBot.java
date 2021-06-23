package ru.simbir.internship.chat.service.bot;

import ru.simbir.internship.chat.dto.MessageDto;

import java.util.List;

public interface RoomBot {
    List<MessageDto> create(String command);

    List<MessageDto> remove(String command);

    List<MessageDto> rename(String command);

    List<MessageDto> connect(String command);

    List<MessageDto> disconnect(String command);
}
