package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.LoginRequestDto;
import ru.simbir.internship.chat.dto.TokenResponseDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/login")
@Validated
public class LoginController extends ParentController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public TokenResponseDto login(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody LoginRequestDto dto) {
        return new TokenResponseDto("TODO-SHECHKA");
    }
}
