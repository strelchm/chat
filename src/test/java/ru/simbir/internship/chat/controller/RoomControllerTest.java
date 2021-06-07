package ru.simbir.internship.chat.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.simbir.internship.chat.domain.User;

import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;


import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

/*    final String PREFIX = "/api/rooms";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @WithMockUser(username = "client", roles = {"CLIENT"})
    @Test
    public void whenAuthRequestThenReturn200() throws Exception {
        String tokenPrefix = "Bearer ";
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        String token = jwtTokenService.generateToken(user);
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .header("Authorization", tokenPrefix+token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

}