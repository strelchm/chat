package ru.simbir.internship.chat.util;

import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.dto.UserRoomDto;

import java.util.stream.Collectors;

@Service
public class MappingUtil {

    public static MessageDto mapToMessageDto(Message entity) {
        MessageDto dto = new MessageDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setUser(mapToUserDto(entity.getUser()));
        dto.setRoom(mapToRoomDto(entity.getRoom()));
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static Message mapToMessageEntity(MessageDto dto) {
        Message entity = new Message();
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        entity.setUser(mapToUserEntity(dto.getUser()));
        entity.setRoom(mapToRoomEntity(dto.getRoom()));
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        return entity;
    }

    public static RoomDto mapToRoomDto(Room entity){
        RoomDto dto = new RoomDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setMessages(entity.getMessages().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toSet()));
        dto.setUserRooms(entity.getUserRooms().stream().map(MappingUtil::mapToUserRoomDto).collect(Collectors.toSet()));
        dto.setType(entity.getType());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static Room mapToRoomEntity(RoomDto dto){
        Room entity = new Room();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setMessages(dto.getMessages().stream().map(MappingUtil::mapToMessageEntity).collect(Collectors.toSet()));
        entity.setUserRooms(dto.getUserRooms().stream().map(MappingUtil::mapToUserRoomEntity).collect(Collectors.toSet()));
        entity.setType(dto.getType());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        return entity;
    }

    public static UserDto mapToUserDto(User entity){
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setMessages(entity.getMessages().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toSet()));
        dto.setUserRooms(entity.getUserRooms().stream().map(MappingUtil::mapToUserRoomDto).collect(Collectors.toSet()));
        dto.setUserAppRoles(entity.getUserAppRoles());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static User mapToUserEntity(UserDto dto){
        User entity = new User();
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
//        entity.setMessages(dto.getMessages().stream().map(MappingUtil::mapToMessageEntity).collect(Collectors.toSet()));
//        entity.setUserRooms(dto.getUserRooms().stream().map(MappingUtil::mapToUserRoomEntity).collect(Collectors.toSet()));
//        entity.setUserAppRoles(dto.getUserAppRoles());
        return entity;
    }

    public static UserRoomDto mapToUserRoomDto(UserRoom entity){
        UserRoomDto dto = new UserRoomDto();
        dto.setUser(mapToUserDto(entity.getUser()));
        dto.setRoom(mapToRoomDto(entity.getRoom()));
        dto.setUserRoomRoles(entity.getUserRoomRoles());
        dto.setBlockedTime(entity.getBlockedTime());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static UserRoom mapToUserRoomEntity(UserRoomDto dto){
        UserRoom entity = new UserRoom();
        entity.setUser(mapToUserEntity(dto.getUser()));
        entity.setRoom(mapToRoomEntity(dto.getRoom()));
        entity.setUserRoomRoles(dto.getUserRoomRoles());
        entity.setBlockedTime(dto.getBlockedTime());
        entity.setCreated(dto.getCreated());
        entity.setUpdated(dto.getUpdated());
        return entity;
    }
}
