package ru.simbir.internship.chat.service.bot;

import ru.simbir.internship.chat.dto.MessageDto;

import java.util.List;

public interface yBot {
    List<MessageDto> channelInfo(String command);

    List<MessageDto> videoCommentRandom(String command);

    List<MessageDto> find(String command);

    List<MessageDto> help();

    MessageDto createMessageDto(String text);
}
