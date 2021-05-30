package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserStatus;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController extends ParentController {
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder encoder) {
        super(userService);
        this.userService = userService;
        this.encoder = encoder;
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
        dto.setStatus(UserStatus.ACTIVE);
        dto.setPassword(encoder.encode(dto.getPassword()));
        Set<UserAppRole> roles = new HashSet<>();
        roles.add(UserAppRole.CLIENT);
        dto.setUserAppRoles(roles);
        return new IdDto(userService.add(dto));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@NotNull(message = NULL_UPDATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto) {
        return userService.edit(dto);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id,
                             @NotNull(message = NULL_PATCH_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody UserDto dto) {
        UserDto roomDto = userService.getById(id);

        if (!roomDto.getLogin().equals(dto.getLogin())) {
            roomDto.setLogin(dto.getLogin());
        }

        if (!roomDto.getStatus().equals(dto.getStatus())) {
            roomDto.setStatus(dto.getStatus());
        }

        return userService.edit(roomDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@NotNull(message = NULL_ID_REQUEST_EXCEPTION) @Validated @PathVariable UUID id) {
        userService.delete(id);
    }
}
