package ru.simbir.internship.chat.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserRoomDto;

import java.util.stream.Collectors;

@Mapper
public interface UserRoomMapper {
    UserRoomMapper INSTANCE = Mappers.getMapper(UserRoomMapper.class);

    default UserRoomDto toDto(UserRoom entity) {
        if (entity == null) return null;
        UserRoomDto dto = new UserRoomDto();
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getRoom() != null) {
            dto.setRoomId(entity.getRoom().getId());
        }
        dto.setUserRoomRole(entity.getUserRoomRole());
        dto.setBlockedTime(entity.getBlockedTime());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    UserRoom fromDto(UserRoomDto dto);
}
