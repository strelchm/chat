package ru.simbir.internship.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.simbir.internship.chat.dto.LoginRequestDto;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    static final String PREFIX = "/api/login";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext context;

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    UserService userService;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void whenValidLoginAndPasswordThenGenerateToken() throws Exception {
        LoginRequestDto auth = new LoginRequestDto("client#1", "client#1");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void whenInvalidLoginThenReturn403() throws Exception {
        LoginRequestDto auth = new LoginRequestDto("client#1", "wrongPassword");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenInvalidPasswordThenReturn403() throws Exception {
        LoginRequestDto auth = new LoginRequestDto(null, "client#1");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isForbidden());
    }
}