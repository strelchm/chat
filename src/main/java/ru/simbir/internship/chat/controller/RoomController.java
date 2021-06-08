package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.UserContext;
import ru.simbir.internship.chat.dto.WrapperDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.RoomService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@Validated
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
public class RoomController extends ParentController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService, UserService userService) {
        super(userService);
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDto> getAllRooms(@ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return roomService.getAll(userContext.getUser().get());
    }

    @GetMapping("/own")
    public List<RoomDto> getAllRoomsByUser(@ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return roomService.getAllByUser(userContext.getUser().get());
    }

    @GetMapping("/{id}")
    public RoomDto getRoomById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                               @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return roomService.getById(id, userContext.getUser().get());
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createRoom(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto,
                            @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        return new IdDto(roomService.add(dto, userContext.getUser().get()));
    }

    @PutMapping("/{id}")
    public RoomDto updateRoom(@NotNull(message = NULL_UPDATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto,
                              @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                              @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        return roomService.edit(dto, userContext.getUser().get());
    }

    @PatchMapping("/{id}")
    public RoomDto patchRoom(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody RoomDto dto,
                             @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }

        RoomDto roomDto = roomService.getById(id, userContext.getUser().get());

        if (!roomDto.getName().equals(dto.getName())) {
            roomDto.setName(dto.getName());
        }

        if (!roomDto.getType().equals(dto.getType())) {
            roomDto.setType(dto.getType());
        }

        return roomService.edit(roomDto, userContext.getUser().get());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRoom(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        roomService.delete(id, userContext.getUser().get());
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(value = HttpStatus.OK)
    public void addMembers(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody WrapperDto<UUID[]> memberId,
                           @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        roomService.addMembers(id, userContext.getUser().get(), memberId.getValue());
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMembers(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @PathVariable UUID memberId,
                           @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        roomService.removeMembers(id, userContext.getUser().get(), memberId);
    }

    @PostMapping("/{id}/moderators")
    @ResponseStatus(value = HttpStatus.OK)
    public void addModerator(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @RequestBody IdDto moderatorId,
                           @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        roomService.moderatorAdd(id, userContext.getUser().get(), moderatorId.getId());
    }

    @DeleteMapping("/{id}/moderators/{moderatorId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteModerator(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID moderatorId,
                             @NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        roomService.moderatorRemove(id, userContext.getUser().get(), moderatorId);
    }

}
