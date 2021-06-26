package ru.simbir.internship.chat.service.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simbir.internship.chat.domain.*;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserRoomService;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoomBotImpl implements RoomBot{
    private final Logger logger = Logger.getLogger(RoomBotImpl.class.getName());
    private final YBot yBot;
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomService userRoomService;

    @Autowired
    public RoomBotImpl(YBot yBot, RoomService roomService, UserService userService, UserRoomService userRoomService) {
        this.yBot = yBot;
        this.roomService = roomService;
        this.userService = userService;
        this.userRoomService = userRoomService;
    }

    @Override
    public List<MessageDto> create(String command, UserDto userDto) {
        int bias = 14; //сколько пропускать символов до начала названия
        boolean isPrivate = false;
        if (command.matches("^//room create -c .+")) {
            bias = bias + 3;
            isPrivate = true;
        };
        RoomDto roomDto = new RoomDto();
        roomDto.setType(isPrivate ? RoomType.PRIVATE : RoomType.PUBLIC);
        String name = command.substring(bias);
        roomDto.setName(name);
        roomService.add(roomDto, userDto);
        return Collections.singletonList(yBot.createMessageDto("Создана комната " + name));
    }

    @Override
    public List<MessageDto> remove(String command, UserDto userDto) {
        int bias = 14; //сколько пропускать символов до начала названия
        String name = command.substring(bias);
        roomService.delete(roomService.getRoomByName(name).getId(), userDto);
        return Collections.singletonList(yBot.createMessageDto("Комната " + name + " удалена."));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MessageDto> rename(String command, UserDto userDto) {
        int bias = 14; //сколько пропускать символов до начала названия
        String separator = " ";
        String[] data = command.substring(bias).split(Pattern.quote(separator));
        if (data.length != 2) {
            logger.severe("Unexpected command");
            return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
        }
        Room room = roomService.getRoomByName(data[0]);
        room.setName(data[1]);
        roomService.edit(MappingUtil.mapToRoomDto(room), userDto);
        return Collections.singletonList(yBot.createMessageDto(
                "Комната " + data[0] + " переименована в " + data[1]));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MessageDto> connect(String command, UserDto userDto) {
        int bias = 15; //сколько пропускать символов до начала названия
        User user;
        String roomName;
        if (command.matches("^//room connect -l \\S+ \\S+$")) {
            bias = bias + 3;
            String separator = " ";
            String[] data = command.substring(bias).split(Pattern.quote(separator));
            if (data.length != 2) {
                logger.severe("Unexpected command");
                return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
            }
            user = userService.getUserById(
                    userService.getUserByLogin(data[0]).orElseThrow(NotFoundException::new).getId());
            roomName = data[1];
        } else {
            user = MappingUtil.mapToUserEntity(userDto);
            roomName = command.substring(bias);
        }
        UUID roomID = roomService.getRoomByName(roomName).getId();
        userRoomService.registerUser(user, roomID, UserRoomRole.USER, userDto);
        return  Collections.singletonList(yBot.createMessageDto(
                "Пользователь " + user.getLogin() + " присоединился к комнате " + roomName));
    }

    @Override
    public List<MessageDto> disconnect(String command, UserDto userDto) {
        int bias = 18; //сколько пропускать символов до начала названия
        User user;
        String roomName;
        Integer penalty = null;
        if (command.matches("^//room disconnect -l \\S+ \\S+$")) {
            bias = bias + 3;
            String separator = " ";
            String[] data = command.substring(bias).split(Pattern.quote(separator));
            if (data.length != 2) {
                logger.severe("Unexpected command");
                return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
            }
            user = userService.getUserById(
                    userService.getUserByLogin(data[0]).orElseThrow(NotFoundException::new).getId());
            roomName = data[1];
        } else if (command.matches("^//room disconnect -l \\S+ -m \\d+ \\S+$")){
            bias = bias + 3;
            String separator = " ";
            String[] data = command.substring(bias).split(Pattern.quote(separator));
            if (data.length != 4) {
                logger.severe("Unexpected command");
                return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
            }
            user = userService.getUserById(
                    userService.getUserByLogin(data[0]).orElseThrow(NotFoundException::new).getId());
            penalty = Integer.parseInt(data[2]);
            roomName = data[3];
        } else if (command.matches("^//room disconnect \\S+$")){
            user = MappingUtil.mapToUserEntity(userDto);
            roomName = command.substring(bias);
        } else {
            return Collections.singletonList(yBot.createMessageDto("Команда не распознана."));
        }
        UUID roomID = roomService.getRoomByName(roomName).getId();
        if (penalty != null) {
            if (penalty > 0) {
                userRoomService.banUser(user, roomID, penalty, userDto);
                return Collections.singletonList(yBot.createMessageDto(
                        String.format("Пользователь %s заблокирован в комнате %s на %d минут.",
                                user.getLogin(), roomName, penalty)));
            } else {
                return Collections.singletonList(
                        yBot.createMessageDto("Количество минут блокировки должно быть больше 0."));
            }
        }
        userRoomService.deregisterUser(user, roomID, userDto);
        return  Collections.singletonList(yBot.createMessageDto(
                "Пользователь " + user.getLogin() + " покинул комнату " + roomName));
    }

    @Override
    public List<MessageDto> help() {
        List<MessageDto> answer =  Arrays.stream(BotCommand.values())
                .map(s -> yBot.createMessageDto(s.getTitle()))
                .collect(Collectors.toList());
        answer.addAll(yBot.help());
        return answer;
    }
}
