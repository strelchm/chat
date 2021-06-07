package ru.simbir.internship.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserStatus;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.repository.MessageRepository;
import ru.simbir.internship.chat.repository.RoomRepository;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final String PREFIX = "/api/users";

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
    public void whenAnonymousThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenAuthRequestThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenGetByIdRequestThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostDtoThenReturn201() throws Exception {
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
    @WithMockUser(username = "client#5", roles = {"CLIENT"})
    public void whenClientPatchHimselfThenReturn200() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("client#5_");
        dto.setPassword("$2a$10$cEatgB3qYBISVXLftQq4P.rlhkQWRoOgBOSh38..FvePGU1tMAnNG");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenClientPatchAnotherThenReturn403() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("client#5_");
        dto.setPassword("$2a$10$cEatgB3qYBISVXLftQq4P.rlhkQWRoOgBOSh38..FvePGU1tMAnNG");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminPatchThenReturn200() throws Exception {
        UserDto dto = new UserDto();
        dto.setLogin("client#7");
        dto.setPassword("client#7");
        dto.setUserAppRole(UserAppRole.CLIENT);
        dto.setStatus(UserStatus.ACTIVE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000007"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#8", roles = {"CLIENT"})
    public void whenClientDeleteHimselfThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000008"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#9", roles = {"CLIENT"})
    public void whenClientDeleteAnotherThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000010"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminDeleteThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000011"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientPostBlockThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/block"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientPostUnblockThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/unblock"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminPostBlockThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/block"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminPostUnblockThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("//00000000-0000-0000-0000-000000000001/unblock"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}