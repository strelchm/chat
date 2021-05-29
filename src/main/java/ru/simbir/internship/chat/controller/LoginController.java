package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.LoginRequestDto;
import ru.simbir.internship.chat.dto.TokenResponseDto;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/login")
@Validated
public class LoginController extends ParentController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository, JwtTokenService tokenService) {
        super(userService);
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping
    public TokenResponseDto login(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody LoginRequestDto dto) {
        User user = userRepository.findByLogin(dto.getLogin()).orElseThrow(() -> new AccessDeniedException("Wrong login"));

        if (!user.getPassword().equals(dto.getPassword())) { // todo: password encoding
            throw new AccessDeniedException("Wrong login or password");
        }

        return new TokenResponseDto(tokenService.generateToken(user));
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "hello admin";
    }

    @GetMapping("/hello/user")
    public String helloUser() {
        UserDto userPrincipal = (UserDto) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return "hello " + userPrincipal;
    }
}
