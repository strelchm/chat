package ru.simbir.internship.chat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageService {
    Page<MessageDto> getAll(Pageable pageable, UUID chatRoomId, UserDto userDto);

    MessageDto getById(UUID messageId, UUID chatRoomId, UserDto userDto);

    UUID add(MessageDto dto, UUID chatRoomId, UserDto userDto);

    MessageDto save(MessageDto dto, UUID roomID, UserDto userDto);

    Set<MessageDto> findAll(UUID roomId, UUID userID );

    MessageDto edit(MessageDto dto, UUID chatRoomId, UserDto userDto);

    void delete(UUID messageId, UUID chatRoomId, UserDto userDto);

    MessageDto process(MessageDto dto, UUID roomID, UserDto userDto);

    List<MessageDto> processBot(MessageDto dto, UserDto userDto);
}
