package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.UserContext;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@Validated
//@PreAuthorize("hasAnyRole()") todo - держать открытой регистрацию
public class UserController extends ParentController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IdDto createUser(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto) {
        return new IdDto(userService.add(dto));
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto,
                             @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        if (dto.getId() == null) {
            dto.setId(id);
        } else if (!id.equals(dto.getId())) {
            throw new BadRequestException();
        }
        UserDto userDto = userService.getById(id);

        if (!userDto.getLogin().equals(dto.getLogin())) {
            userDto.setLogin(dto.getLogin());
        }

        if (!userDto.getStatus().equals(dto.getStatus())) {
            userDto.setStatus(dto.getStatus());
        }

        return userService.edit(userDto, userContext.getUser().get());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        userService.delete(id, userContext.getUser().get());
    }

    @PostMapping("/{id}/block")
    @ResponseStatus(value = HttpStatus.OK)
    public void blockUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                           @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        userService.blockUser(id, userContext.getUser().get());
    }

    @PostMapping("/{id}/unblock")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unblockUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                          @ModelAttribute(USER_CONTEXT) UserContext userContext) {
        userService.unblockUser(id, userContext.getUser().get());
    }
}
