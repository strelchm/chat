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
import ru.simbir.internship.chat.domain.RoomType;
import ru.simbir.internship.chat.dto.RoomDto;


import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

    static final String PREFIX = "/api/rooms";
    static final ObjectMapper objectMapper = new ObjectMapper();

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
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenAuthRequestThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenAuthRequestAtOwnThenReturn200() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX.concat("/own"))
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
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostRoomDtoThenReturn201() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX)
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

 /*   @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPutThenReturn200() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PRIVATE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

 /*   @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPatchThenReturn200() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PRIVATE);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostMemberThenReturn200() throws Exception {
        String json = "[\"00000000-0000-0000-0000-000000000002\"]";
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteMemberThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenPostModeratorThenReturn200() throws Exception {
        String json = "\"00000000-0000-0000-0000-000000000002\"";
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/moderators"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

/*    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenDeleteModeratorThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/moderators/00000000-0000-0000-0000-000000000002"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }*/

  /*  @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostModeratorByClientThenReturn403() throws Exception {
        String json = "\"00000000-0000-0000-0000-000000000002\"";
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/moderators"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteModeratorByClentThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/moderators/00000000-0000-0000-0000-000000000002"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }*/

    @Test
    public void whenAnonymousThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(PREFIX)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

  /*  @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }*/






}