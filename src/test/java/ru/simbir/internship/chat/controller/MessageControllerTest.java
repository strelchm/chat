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
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.dto.UserDto;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {

    private static final String PREFIX = "/api";

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
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenAuthRequestThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenGetByIdRequestThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostDtoThenReturn201() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "client#14", roles = {"CLIENT"})
    public void whenPostByNotMemberThenReturn403() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenPostByBlockedUserThenReturn403() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#6", roles = {"CLIENT"})
    public void whenPostByGlobaLBlockedUserThenReturn403() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("AAA");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientPutHimselfThenReturn200() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientPutAnotherThenReturn403() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000009"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminPutThenReturn200() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000004"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenModeratorPutThenReturn200() throws Exception {
        MessageDto dto = new MessageDto();
        dto.setText("BBB");
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000008"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientDeleteHimselfThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenClientOwnerDeleteAnotherThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000002"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenClienDeleteAnotherThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000001/messages/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenBlockedUserDeleteThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000006"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAdminDeleteThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000002/messages/00000000-0000-0000-0000-000000000003"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenModeratorDeleteThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/rooms/00000000-0000-0000-0000-000000000004/messages/00000000-0000-0000-0000-000000000007"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}