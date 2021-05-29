package ru.simbir.internship.chat.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.UserDto;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String JWT_SECRET = "6g}^S,z@Fq33<SPZ_o7V1.W^gEN;)n1YknguFiiTsboaNt%JW!<f<V?s{iBrU9R";
    private static final int EXPIRATION_HOURS = 3;

    private static final String ID_CLAIM_NAME = "id";
    private static final String LOGIN_CLAIM_NAME = "login";
    private static final String ROLE_CLAIM_NAME = "role";

    @Override
    public String generateToken(User user) {
        Instant expirationTime = Instant.now().plus(EXPIRATION_HOURS, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String compactTokenString = Jwts.builder()
                .claim(ID_CLAIM_NAME, user.getId())
                .claim(LOGIN_CLAIM_NAME, user.getLogin())
                .claim(ROLE_CLAIM_NAME, user.getUserAppRoles())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TOKEN_PREFIX + compactTokenString;
    }

    @Override
    public UserDto parseToken(String token) {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        UserDto userDto = new UserDto();
        userDto.setId(jwsClaims.getBody().get(ID_CLAIM_NAME, UUID.class));
        userDto.setLogin(jwsClaims.getBody().get(LOGIN_CLAIM_NAME, String.class));
        userDto.setUserAppRoles(jwsClaims.getBody().get(ROLE_CLAIM_NAME, Set.class));

        return userDto;
    }
}