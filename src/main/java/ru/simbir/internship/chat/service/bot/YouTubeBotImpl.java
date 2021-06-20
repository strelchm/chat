package ru.simbir.internship.chat.service.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.service.impl.BotCommand;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class YouTubeBotImpl implements YouTubeBot{
    @Value("${chat.google-api-key}")
    private String key;

    @Override
    public List<MessageDto> channelInfo(String command) {
        return null;
    }

    @Override
    public List<MessageDto> videoCommentRandom(String command) {
        return null;
    }

    @Override
    public List<MessageDto> find(String command) {
        return null;
    }

    @Override
    public List<MessageDto> help() {
        return Arrays.stream(BotCommand.values())
                .map(c-> new MessageDto(c.getTitle(), new Date()))
                .collect(Collectors.toList());
    }
}
