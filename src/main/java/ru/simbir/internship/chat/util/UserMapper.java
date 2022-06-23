package ru.simbir.internship.chat.util;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.UserDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", expression = "java(null)")
    UserDto toDto(User user);

    User fromDto(UserDto dto);
}
