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
    private final YBot yBot;
    private final RoomService roomService;
    private final MessageService messageService;
    private final RoomAndUserBot roomAndUserBot;

    @Autowired
    public WebSocketServiceImpl(YBot yBot, RoomService roomService, MessageService messageService, RoomAndUserBot roomAndUserBot) {
        this.yBot = yBot;
        this.roomService = roomService;
        this.messageService = messageService;
        this.roomAndUserBot = roomAndUserBot;
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
        try {
            if (command.matches(YBotCommand.YBOT_CHANNEL_INFO.getRegex())) {
                return yBot.channelInfo(command);
            }
            if (command.matches(YBotCommand.YBOT_HELP.getRegex())) {
                return yBot.help();
            }
            if (command.matches(YBotCommand.YBOT_VIDEO_COMMENT_RANDOM.getRegex())) {
                return yBot.videoCommentRandom(command);
            }
            if (command.matches(YBotCommand.YBOT_FIND.getRegex())) {
                return yBot.find(command);
            }
            if (command.matches(RoomAndUserBotCommand.ROOM_REMOVE.getRegex())) {
                return roomAndUserBot.remove(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.ROOM_CREATE.getRegex())) {
                return roomAndUserBot.create(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.HELP.getRegex())) {
                return roomAndUserBot.help();
            }
            if (command.matches(RoomAndUserBotCommand.ROOM_RENAME.getRegex())) {
                return roomAndUserBot.rename(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.ROOM_CONNECT.getRegex())) {
                return roomAndUserBot.connect(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.ROOM_DISCONNECT.getRegex())) {
                return roomAndUserBot.disconnect(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.USER_RENAME.getRegex())) {
                return roomAndUserBot.loginRename(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.USER_BAN.getRegex())) {
                return roomAndUserBot.userBan(command, userDto);
            }
            if (command.matches(RoomAndUserBotCommand.USER_MODERATOR.getRegex())) {
                return roomAndUserBot.moderatorEdit(command, userDto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.singletonList(yBot.createMessageDto(ex.getMessage()));
        }
        return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
    }
}
