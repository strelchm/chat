package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.RoomType;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.WebSocketService;
import ru.simbir.internship.chat.service.bot.BotCommand;
import ru.simbir.internship.chat.service.bot.YouTubeBot;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final YouTubeBot youTubeBot;
    private final RoomService roomService;
    private final MessageService messageService;

    @Autowired
    public WebSocketServiceImpl(YouTubeBot youTubeBot, RoomService roomService, MessageService messageService) {
        this.youTubeBot = youTubeBot;
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @Override
    public MessageDto process(MessageDto messageDto, UUID roomID, UserDto userDto) {
        if (messageDto == null || roomID == null || userDto == null) {
            throw new BadRequestException();
        }
        if (messageDto.getRoomId() == null) {
            messageDto.setRoomId(roomID);
        }
        if (!messageDto.getRoomId().equals(roomID)) {
            throw new BadRequestException();
        }
        if (messageDto.getId() == null) {
            if (messageDto.getUserId() == null) {
                messageDto.setUserId(userDto.getId());
            }
            if (!messageDto.getUserId().equals(userDto.getId())) {
                throw new BadRequestException();
            }
            return messageService.save(messageDto, roomID, userDto);
        } else {
            return messageService.edit(messageDto, roomID, userDto);
        }
    }

    @Override
    public List<MessageDto> processBot(MessageDto messageDto) {
        String command = messageDto.getText();
        if (command.matches(BotCommand.YBOT_CHANNEL_INFO.getRegex())) {
            return youTubeBot.channelInfo(command);
        }
        if (command.matches(BotCommand.YBOT_HELP.getRegex())) {
            return youTubeBot.help();
        }
        if (command.matches(BotCommand.YBOT_VIDEO_COMMENT_RANDOM.getRegex())) {
            return youTubeBot.videoCommentRandom(command);
        }
        if (command.matches(BotCommand.YBOT_FIND.getRegex())) {
            return youTubeBot.find(command);
        }
        return Collections.singletonList(youTubeBot.createMessageDto("Команда не распознана."));
    }
}
