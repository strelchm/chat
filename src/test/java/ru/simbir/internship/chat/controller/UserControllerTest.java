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
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserStatus;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    static final String PREFIX = "/api/users";

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
                .get(PREFIX)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAuthRequestThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetByIdRequestThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostDtoThenReturn201() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("AAA");
        dto.setPassword("BBB");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX)
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void whenClientPatchHimselfThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000005"));
        UserDto dto = new UserDto();
        dto.setLogin("client#5_");
        dto.setPassword("$2a$10$cEatgB3qYBISVXLftQq4P.rlhkQWRoOgBOSh38..FvePGU1tMAnNG");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenClientPatchAnotherThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        UserDto dto = new UserDto();
        dto.setLogin("client#5_");
        dto.setPassword("$2a$10$cEatgB3qYBISVXLftQq4P.rlhkQWRoOgBOSh38..FvePGU1tMAnNG");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminPatchThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        UserDto dto = new UserDto();
        dto.setLogin("client#7");
        dto.setPassword("client#7");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000007"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenClientDeleteHimselfThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000008"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000008"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenClientDeleteAnotherThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000009"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000010"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminDeleteThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000011"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenClientPostBlockThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/block"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenClientPostUnblockThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/unblock"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminPostBlockThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/block"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAdminPostUnblockThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/unblock"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}