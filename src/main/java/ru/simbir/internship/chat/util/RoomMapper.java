package ru.simbir.internship.chat.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.dto.RoomDto;

import java.util.stream.Collectors;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    default RoomDto toDto(Room entity) {
        if (entity == null) return null;
        RoomDto dto = new RoomDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (entity.getUserRooms() != null) {
            dto.setUserRooms(entity.getUserRooms().stream().map(MappingUtil::mapToUserRoomDto).collect(Collectors.toSet()));
        }
        dto.setType(entity.getType());
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    Room fromDto(RoomDto dto);
}
