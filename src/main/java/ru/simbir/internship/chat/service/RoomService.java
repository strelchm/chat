package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.dto.RoomDto;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomDto> getAll();
    RoomDto getById(UUID id);
    UUID add(RoomDto dto);
    RoomDto edit(RoomDto dto);
    RoomDto delete(RoomDto dto);
}
