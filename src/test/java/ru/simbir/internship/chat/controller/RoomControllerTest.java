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
import ru.simbir.internship.chat.domain.RoomType;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.WrapperDto;
import ru.simbir.internship.chat.service.JwtTokenService;
import ru.simbir.internship.chat.service.UserService;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

    static final String PREFIX = "/api/rooms";

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
    public void whenAuthRequestThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAuthRequestAtOwnThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/own"))
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
    void whenPostRoomDtoThenReturn201() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX)
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPostByGlobalBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000006"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX)
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutByOwnerThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutByUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutByBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutByModeratorThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000005"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPatchByOwnerThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPatchByUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPatchByBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPatchByModeratorThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
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
    void whenDeleteByOwnerThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000003"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000003"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteByUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteByBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteByModeratorThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostMemberThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        WrapperDto<UUID[]> dto = new WrapperDto<>();
        dto.setValue(new UUID[]{UUID.fromString("00000000-0000-0000-0000-000000000015")});
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostMemberByBlokedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        WrapperDto<UUID[]> dto = new WrapperDto<>();
        dto.setValue(new UUID[]{UUID.fromString("00000000-0000-0000-0000-000000000015")});
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteMemberByOwnerThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteMemberByUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteMemberByBlockedUserThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteMemberByModeratorThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000005/members/00000000-0000-0000-0000-000000000002"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPostModeratorThenReturn200() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        IdDto dto = new IdDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000014"));
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteModeratorThenReturn204() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPostModeratorByClientThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        IdDto dto = new IdDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000014"));
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteModeratorByClentThenReturn403() throws Exception {
        User user = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators/00000000-0000-0000-0000-000000000005"))
                .header("Authorization", "Bearer " + jwtTokenService.generateToken(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}