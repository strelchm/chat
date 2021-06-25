package ru.simbir.internship.chat.service.bot;

import ru.simbir.internship.chat.dto.MessageDto;

import java.util.List;

public interface UserBot {
    List<MessageDto> rename(String command);

    List<MessageDto> ban(String command);

    List<MessageDto> moderator(String command);
}
