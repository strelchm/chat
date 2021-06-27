package ru.simbir.internship.chat.service.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simbir.internship.chat.domain.*;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserRoomService;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoomAndUserBotImpl implements RoomAndUserBot {
    private final Logger logger = Logger.getLogger(RoomAndUserBotImpl.class.getName());
    private final YBot yBot;
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomService userRoomService;

    @Autowired
    public RoomAndUserBotImpl(YBot yBot, RoomService roomService, UserService userService, UserRoomService userRoomService) {
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
        }
        ;
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
        return Collections.singletonList(yBot.createMessageDto(
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
        } else if (command.matches("^//room disconnect -l \\S+ -m \\d+ \\S+$")) {
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
        } else if (command.matches("^//room disconnect \\S+$")) {
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
        return Collections.singletonList(yBot.createMessageDto(
                "Пользователь " + user.getLogin() + " покинул комнату " + roomName));
    }

    @Override
    public List<MessageDto> loginRename(String command, UserDto userDto) {
        String[] names = command.substring(14).split(" ");
        if (names.length == 1) {
            return selfRename(names[0], userDto);
        } else if (names.length == 2) {
            return userRename(names[0], names[1], userDto);
        } else {
            throw new BadRequestException();
        }
    }

    private List<MessageDto> selfRename(String newName, UserDto userDto) {
        String oldName = userDto.getLogin();
        userDto.setLogin(newName);
        userService.edit(userDto, userDto);
        return Collections.singletonList(yBot.createMessageDto(
                "Изменение логина с " + oldName + " на " + userDto.getLogin())
        );
    }

    private List<MessageDto> userRename(String oldName, String newName, UserDto userDto) {
        UserDto editUser = userService.getUserByLogin(oldName).orElseThrow(NotFoundException::new);
        editUser.setLogin(newName);
        userService.edit(editUser, userDto);
        return Collections.singletonList(yBot.createMessageDto(
                "Изменение логина с " + oldName + " на " + newName)
        );
    }

    public List<MessageDto> userBan(String command, UserDto userDto) {
        String[] names = command.substring(14).split(" ");
        if (names.length == 1) {
            return foreverBan(names[0], userDto);
        } else if (names.length == 3) {
            return ban(command, userDto);
        } else {
            throw new BadRequestException();
        }
    }

    private List<MessageDto> foreverBan(String login, UserDto userDto) {
        UserDto editUser = userService.getUserByLogin(login).orElseThrow(NotFoundException::new);
        List<UserRoom> memberships = userRoomService.getAllByUserId(editUser.getId());

        memberships.forEach(v -> roomService.removeMembers(v.getRoom().getId(), userDto, v.getUser().getId()));

        userService.blockUser(editUser.getId(), userDto);
        return Collections.singletonList(yBot.createMessageDto(
                "Пользователь " + editUser.getLogin() + " успешно заблокирован")
        );
    }

    private List<MessageDto> ban(String command, UserDto userDto) {
        String[] args = command.substring(14).split(" ");
        UserDto editUser = userService.getUserByLogin(args[0]).orElseThrow(NotFoundException::new);
        List<UserRoom> memberships = userRoomService.getAllByUserId(editUser.getId());
        memberships.forEach(v -> {
                    try {
                        userRoomService.banUser(
                                MappingUtil.mapToUserEntity(editUser),
                                v.getRoom().getId(), Integer.parseInt(args[2]), userDto);
                    } catch (Exception e) {
                        logger.severe("Can't ban owner " + editUser.getLogin() + " from room " + v.getId());
                    }
                }
        );
        return Collections.singletonList(yBot.createMessageDto(
                "Пользователь " + editUser.getLogin() + " успешно заблокирован на " + args[2] + " минуты")
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<MessageDto> moderatorEdit(String command, UserDto userDto) {
        if (!userDto.getUserAppRole().equals(UserAppRole.ADMIN)) {
            throw new AccessDeniedException("Only admin can edit moderators.");
        }
        String[] args = command.substring(17).split(" ");
        System.out.println(Arrays.toString(args));
        if (args[0].equals("-n")) {
            User user = userService.getUserById(
                    userService.getUserByLogin(args[2]).orElseThrow(NotFoundException::new).getId());
            UUID roomID = roomService.getRoomByName(args[1]).getId();
            userRoomService.registerUser(user, roomID, UserRoomRole.MODERATOR, userDto);
            return Collections.singletonList(yBot.createMessageDto(
                    String.format("Пользователь %s назначен модератором в комнате %s.", args[2], args[1])));
        } else if (args[0].equals("-d")) {
            User user = userService.getUserById(
                    userService.getUserByLogin(args[2]).orElseThrow(NotFoundException::new).getId());
            UUID roomID = roomService.getRoomByName(args[1]).getId();
            userRoomService.registerUser(user, roomID, UserRoomRole.USER, userDto);
            return Collections.singletonList(yBot.createMessageDto(
                    String.format("Пользователь %s назначен простым пользователем в комнате %s.", args[2], args[1])));
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public List<MessageDto> help() {
        List<MessageDto> answer = Arrays.stream(RoomAndUserBotCommand.values())
                .map(s -> yBot.createMessageDto(s.getTitle()))
                .collect(Collectors.toList());
        answer.addAll(yBot.help());
        return answer;
    }
}
