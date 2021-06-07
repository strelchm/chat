package ru.simbir.internship.chat.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.simbir.internship.chat.domain.Message;
import ru.simbir.internship.chat.dto.MessageDto;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto toDto(Message message);

    Message fromDto(MessageDto dto);
}
