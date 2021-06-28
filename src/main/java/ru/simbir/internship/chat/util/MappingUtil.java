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

@Service
public class MappingUtil {
    public static MessageDto mapToMessageDto(Message entity) {
        return MessageMapper.INSTANCE.toDto(entity);
    }

    public static Message mapToMessageEntity(MessageDto dto) {
        return MessageMapper.INSTANCE.fromDto(dto);
    }

    public static RoomDto mapToRoomDto(Room entity) {
        return RoomMapper.INSTANCE.toDto(entity);
    }

    public static Room mapToRoomEntity(RoomDto dto) {
        return RoomMapper.INSTANCE.fromDto(dto);
    }

    public static UserDto mapToUserDto(User entity) {
        return UserMapper.INSTANCE.toDto(entity);
    }

    public static User mapToUserEntity(UserDto dto) {
        return UserMapper.INSTANCE.fromDto(dto);
    }

    public static UserRoomDto mapToUserRoomDto(UserRoom entity) {
        return UserRoomMapper.INSTANCE.toDto(entity);
    }

    public static UserRoom mapToUserRoomEntity(UserRoomDto dto) {
        return UserRoomMapper.INSTANCE.fromDto(dto);
    }
}
