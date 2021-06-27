package ru.simbir.internship.chat.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.dto.MessageDto;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "userId", expression = "java(message.getUser().getId())")
    @Mapping(target = "roomId", expression = "java(message.getRoom().getId())")
    @Mapping(target = "userLogin", expression = "java(message.getUser().getLogin())")
    MessageDto toDto(Message message);

    Message fromDto(MessageDto dto);
}
