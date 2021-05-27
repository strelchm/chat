package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDto> getAll();
    MessageDto getById(UUID id);
    UUID add(MessageDto dto);
    MessageDto edit(MessageDto dto);
    MessageDto delete(MessageDto dto);
}
