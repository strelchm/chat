package ru.simbir.internship.chat.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.repository.BlockedUserRepository;
import ru.simbir.internship.chat.service.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.simbir.internship.chat.service.impl.JwtTokenServiceImpl.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService tokenService;
    private final BlockedUserRepository blockedUserRepository;

    public JwtAuthenticationFilter(JwtTokenService tokenService, BlockedUserRepository blockedUserRepository) {
        this.tokenService = tokenService;
        this.blockedUserRepository = blockedUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws IOException, ServletException {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = authorizationHeader.replace(TOKEN_PREFIX, "");
        UserDto userPrincipal = tokenService.parseToken(token);

        if(blockedUserRepository.findById(userPrincipal.getId()).isPresent()) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write("User is blocked");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(createToken(userPrincipal));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    private UsernamePasswordAuthenticationToken createToken(UserDto userPrincipal) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (userPrincipal.getUserAppRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userPrincipal.getUserAppRole().name()));
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
    }
}
