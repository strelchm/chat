package ru.simbir.internship.chat.service;

import org.springframework.security.access.annotation.Secured;
import ru.simbir.internship.chat.domain.UserRoom;
import ru.simbir.internship.chat.domain.UserRoomRole;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomDto> getAll(UserDto userDto);

    List<RoomDto> getAllByUser(UserDto userDto);

    RoomDto getById(UUID id, UserDto userDto);

    UUID add(RoomDto dto, UserDto userDto);

    RoomDto edit(RoomDto dto, UserDto userDto);

    void delete(UUID id, UserDto userDto);

    void addMembers(UUID roomId, UserDto userDto, UUID... idArray);

    void removeMembers(UUID roomId, UserDto userDto, UUID... idArray);

    void moderatorAdd(UUID roomId, UserDto userDto, UUID moderatorId);

    void moderatorRemove(UUID roomId, UserDto userDto, UUID moderatorId);
}
