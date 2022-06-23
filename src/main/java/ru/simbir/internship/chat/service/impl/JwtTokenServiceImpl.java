package ru.simbir.internship.chat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.service.JwtTokenService;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String JWT_SECRET = "6g}^S,z@Fq33<SPZ_o7V1.W^gEN;)n1YknguFiiTsboaNt%JW!<f<V?s{iBrU9R";
    private static final int EXPIRATION_HOURS = 3;

    private static final String ID_CLAIM_NAME = "id";
    private static final String LOGIN_CLAIM_NAME = "login";
    private static final String ROLE_CLAIM_NAME = "role";

    private static final Logger logger = Logger.getLogger(JwtTokenServiceImpl.class.getName());

    @Override
    public String generateToken(User user) {
        Instant expirationTime = Instant.now().plus(EXPIRATION_HOURS, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        JwtBuilder compactTokenString = Jwts.builder()
                .claim(ID_CLAIM_NAME, user.getId().toString())
                .claim(LOGIN_CLAIM_NAME, user.getLogin());

        try {
            compactTokenString.claim(ROLE_CLAIM_NAME, new ObjectMapper().writeValueAsString(user.getUserAppRole()));
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return compactTokenString.setExpiration(expirationDate).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    @Override
    public UserDto parseToken(String token) {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        UserDto userDto = new UserDto();
        userDto.setId(UUID.fromString(jwsClaims.getBody().get(ID_CLAIM_NAME, String.class)));
        userDto.setLogin(jwsClaims.getBody().get(LOGIN_CLAIM_NAME, String.class));

        try {
            userDto.setUserAppRole(
                    new ObjectMapper().readValue(jwsClaims.getBody().get(ROLE_CLAIM_NAME, String.class),
                    new TypeReference<UserAppRole>() {})
            );
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }

        return userDto;
    }
}