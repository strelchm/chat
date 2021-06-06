package ru.simbir.internship.chat.util;

import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.dto.UserRoomDto;

import java.util.stream.Collectors;

public class MappingUtil {

    public static MessageDto mapToMessageDto(Message entity) {
        if (entity == null) return null;
        MessageDto dto = new MessageDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        if (entity.getUser() != null) {
            dto.setUser(mapToUserDto(entity.getUser()));
        }
        if (entity.getUser() != null) {
            dto.setRoom(mapToRoomDto(entity.getRoom()));
        }
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static Message mapToMessageEntity(MessageDto dto) {
        if (dto == null) return null;
        Message entity = new Message();
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        if (dto.getUser() != null) {
            entity.setUser(mapToUserEntity(dto.getUser()));
        }
        if (dto.getRoom() != null) {
            entity.setRoom(mapToRoomEntity(dto.getRoom()));
        }
        return entity;
    }

    public static RoomDto mapToRoomDto(Room entity) {
        if (entity == null) return null;
        RoomDto dto = new RoomDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (entity.getMessages() != null) {
            dto.setMessages(entity.getMessages().stream().map(MappingUtil::mapToMessageDto).collect(Collectors.toSet()));
        }
        if (entity.getUserRooms() != null) {
            dto.setUserRooms(entity.getUserRooms().stream().map(MappingUtil::mapToUserRoomDto).collect(Collectors.toSet()));
        }
        dto.setType(entity.getType());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static Room mapToRoomEntity(RoomDto dto) {
        if (dto == null) return null;
        Room entity = new Room();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        if (dto.getMessages() != null) {
            entity.setMessages(dto.getMessages().stream().map(MappingUtil::mapToMessageEntity).collect(Collectors.toSet()));
        }
        if (dto.getUserRooms() != null) {
            entity.setUserRooms(dto.getUserRooms().stream().map(MappingUtil::mapToUserRoomEntity).collect(Collectors.toSet()));
        }
        entity.setType(dto.getType());
        return entity;
    }

    public static UserDto mapToUserDto(User entity) {
        if (entity == null) return null;
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setUserAppRole(entity.getUserAppRole());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static User mapToUserEntity(UserDto dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
        entity.setStatus(dto.getStatus());
        if (dto.getUserAppRole() != null) {
            entity.setUserAppRole(dto.getUserAppRole());
        }
        return entity;
    }

    public static UserRoomDto mapToUserRoomDto(UserRoom entity) {
        if (entity == null) return null;
        UserRoomDto dto = new UserRoomDto();
        if (entity.getUser() != null) {
            dto.setUser(mapToUserDto(entity.getUser()));
        }
        dto.setUserRoomRole(entity.getUserRoomRole());
        dto.setBlockedTime(entity.getBlockedTime());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public static UserRoom mapToUserRoomEntity(UserRoomDto dto) {
        if (dto == null) return null;
        UserRoom entity = new UserRoom();
        if (dto.getUser() != null) {
            entity.setUser(mapToUserEntity(dto.getUser()));
        }
        entity.setUserRoomRole(dto.getUserRoomRole());
        return entity;
    }
}
