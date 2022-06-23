package ru.simbir.internship.chat.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.dto.UserRoomDto;

@Mapper
public interface UserRoomMapper {
    UserRoomMapper INSTANCE = Mappers.getMapper(UserRoomMapper.class);

    @Mapping(target = "userId", expression = "java(" +
            "entity.getUser() != null ? entity.getUser().getId() : null" +
            ")")
    @Mapping(target = "roomId", expression = "java(" +
            "entity.getRoom() != null ? entity.getRoom().getId() : null" +
            ")")
    UserRoomDto toDto(UserRoom entity);

    UserRoom fromDto(UserRoomDto dto);
}
