package ru.simbir.internship.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.simbir.internship.chat.dto.LoginRequestDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {
    static final String PREFIX = "/api/login";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void whenValidLoginAndPasswordThenGenerateToken() throws Exception {
        LoginRequestDto auth = new LoginRequestDto("client#1", "client#1");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidLoginThenReturn403() throws Exception {
        LoginRequestDto auth = new LoginRequestDto("client#1", "wrongPassword");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenInvalidPasswordThenReturn403() throws Exception {
        LoginRequestDto auth = new LoginRequestDto(null, "client#1");
        String json = objectMapper.writeValueAsString(auth);
        mvc.perform(post(PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json))
                .andExpect(status().isForbidden());
    }
}