package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.service.RoomService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@Validated
public class RoomController extends ParentController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDto> getAllRooms() {
        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public RoomDto getRoomById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return roomService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createRoom(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto) {
        return new IdDto(roomService.add(dto));
    }

    @PutMapping("/{id}")
    public RoomDto updateRoom(@NotNull(message = NULL_UPDATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto) {
        return roomService.edit(dto);
    }

    @PatchMapping("/{id}")
    public RoomDto patchRoom(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                                @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto) {
        RoomDto roomDto = roomService.getById(id);

        if (!roomDto.getName().equals(dto.getName())) {
            roomDto.setName(dto.getName());
        }

        if (!roomDto.getType().equals(dto.getType())) {
            roomDto.setType(dto.getType());
        }

        return roomService.edit(roomDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public RoomDto deleteRoom(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) { // todo - void return type
        RoomDto messageDto = roomService.getById(id); // todo
        return roomService.delete(messageDto);
    }
}
