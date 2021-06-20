package ru.simbir.internship.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

//  Вспомогательный контроллер для тестирования (подшивает токен)

@Controller
public class TestController {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Autowired
    public TestController(JwtTokenService jwtTokenService, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public void  admin(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws Exception{
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        response.setHeader("Authorization", "Bearer " + jwtTokenService.generateToken(user));
        request.getRequestDispatcher("/").forward(request, response);
    }

    @GetMapping("/client")
    public void client(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        response.setHeader("Authorization", "Bearer " + jwtTokenService.generateToken(user));
        request.getRequestDispatcher("/").forward(request, response);
    }
}
