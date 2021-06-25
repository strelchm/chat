package ru.simbir.internship.chat.service.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.RoomType;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.repository.UserRoomRepository;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomBotImpl implements RoomBot{

    private final yBot yBot;
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public RoomBotImpl(yBot yBot, RoomService roomService, UserService userService, UserRoomRepository userRoomRepository) {
        this.yBot = yBot;
        this.roomService = roomService;
        this.userService = userService;
        this.userRoomRepository = userRoomRepository;
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
        roomDto.setName(command.substring(bias));
        UUID roomId = roomService.add(roomDto, userDto);
        User user = userService.getUserById(userDto.getId());
        UserRoom userRoom = new UserRoom();
        userRoom.setRoom(roomService.getRoomById(roomId));
        userRoom.setUser(user);
        UserRoom.Key key = new UserRoom.Key();
        key.setRoomId(roomId);
        key.setUserId(user.getId());
        userRoom.setId(key);
        userRoom.setCreated(new Date());
        userRoom.setUserRoomRole(UserRoomRole.OWNER);
        userRoomRepository.save(userRoom);
        return Collections.singletonList(yBot.createMessageDto("Создана комната " + roomId));
    }

    @Override
    public List<MessageDto> remove(String command, UserDto userDto) {
        int bias = 14; //сколько пропускать символов до начала названия
        String name = command.substring(bias);
        roomService.delete(roomService.getRoomByName(name).getId(), userDto);
        return Collections.singletonList(yBot.createMessageDto("Комната " + name + " удалена."));
    }

    @Override
    public List<MessageDto> rename(String command) {
        return null;
    }

    @Override
    public List<MessageDto> connect(String command) {
        return null;
    }

    @Override
    public List<MessageDto> disconnect(String command) {
        return null;
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
