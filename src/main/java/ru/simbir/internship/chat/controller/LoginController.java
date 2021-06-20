package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.LoginRequestDto;
import ru.simbir.internship.chat.dto.TokenResponseDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/login")
@Validated
public class LoginController extends ParentController {
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder encoder;

    @Autowired
    public LoginController(UserService userService, UserRepository userRepository, JwtTokenService tokenService, PasswordEncoder encoder) {
        super(userService);
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.encoder = encoder;
    }

    @PostMapping
    public TokenResponseDto login(@NotNull(message = NULL_CREATE_OBJECT_REQUEST_EXCEPTION) @Validated @RequestBody LoginRequestDto dto) {
        User user = userRepository.findByLogin(dto.getLogin()).orElseThrow(() -> new AccessDeniedException("Wrong login"));
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Wrong password");
        }
        return new TokenResponseDto(tokenService.generateToken(user));
    }
}
