package ru.simbir.internship.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {

    static final String PREFIX = "/api";

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
    void whenAnonymousThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAuthRequestThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetByIdRequestThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostDtoThenReturn201() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPostByNotMemberThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000014"));
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostByBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostByGlobaLBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000006"));
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenClientPutHimselfThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenClientPutAnotherThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000009"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminPutThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000004"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenModeratorPutThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000008"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenClientDeleteHimselfThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenClientOwnerDeleteAnotherThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000002"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenClienDeleteAnotherThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenBlockedUserDeleteThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000006"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminDeleteThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000003"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenModeratorDeleteThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000007"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}