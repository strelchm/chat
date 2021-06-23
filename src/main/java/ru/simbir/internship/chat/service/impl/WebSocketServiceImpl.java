package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.WebSocketService;
import ru.simbir.internship.chat.service.bot.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class WebSocketServiceImpl implements WebSocketService {
    private final yBot yBot;
    private final RoomService roomService;
    private final MessageService messageService;
    private final RoomBot roomBot;
    private final UserBot userBot;

    @Autowired
    public WebSocketServiceImpl(yBot yBot, RoomService roomService, MessageService messageService, RoomBot roomBot, UserBot userBot) {
        this.yBot = yBot;
        this.roomService = roomService;
        this.messageService = messageService;
        this.roomBot = roomBot;
        this.userBot = userBot;
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
    public List<MessageDto> processBot(MessageDto messageDto, UserDto userDto) {
        String command = messageDto.getText();
        if (command.matches(yBotCommand.YBOT_CHANNEL_INFO.getRegex())) {
            return yBot.channelInfo(command);
        }
        if (command.matches(yBotCommand.YBOT_HELP.getRegex())) {
            return yBot.help();
        }
        if (command.matches(yBotCommand.YBOT_VIDEO_COMMENT_RANDOM.getRegex())) {
            return yBot.videoCommentRandom(command);
        }
        if (command.matches(yBotCommand.YBOT_FIND.getRegex())) {
            return yBot.find(command);
        }
        if (command.matches(BotCommand.ROOM_REMOVE.getRegex())) {
            return roomBot.remove(command, userDto);
        }
        if (command.matches(BotCommand.ROOM_CREATE.getRegex())) {
            return roomBot.create(command, userDto);
        }
        if (command.matches(BotCommand.HELP.getRegex())) {
            return roomBot.help();
        }
        return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
    }
}
