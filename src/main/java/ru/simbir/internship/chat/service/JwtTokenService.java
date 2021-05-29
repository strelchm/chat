package ru.simbir.internship.chat.service;

import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.UserDto;

public interface JwtTokenService {
    String generateToken(User user);

    UserDto parseToken(String token);
}