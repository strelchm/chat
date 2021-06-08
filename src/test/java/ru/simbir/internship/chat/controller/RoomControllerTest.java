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
import ru.simbir.internship.chat.dto.IdDto;
import ru.simbir.internship.chat.dto.RoomDto;
import ru.simbir.internship.chat.dto.WrapperDto;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

    static final String PREFIX = "/api/rooms";

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

    @Test
    @WithMockUser(username = "client#6", roles = {"CLIENT"})
    public void whenPostByBlockedUserThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX)
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPutByOwnerThenReturn200() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenPutByUserThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenPutByBlockedUserThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPutByModeratorThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000005"));
        dto.setName("testRoom");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .put(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPatchByOwnerThenReturn200() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenPatchByUserThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenPatchByBlockedUserThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPatchByModeratorThenReturn403() throws Exception {
        RoomDto dto = new RoomDto();
        dto.setName("testRoom2");
        dto.setType(RoomType.PUBLIC);
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .patch(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#3", roles = {"CLIENT"})
    public void whenDeleteByOwnerThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000003"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenDeleteByUserThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenDeleteByBlockedUserThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteByModeratorThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostMemberThenReturn200() throws Exception {
        WrapperDto<UUID[]> dto = new WrapperDto<>();
        dto.setValue(new UUID[]{UUID.fromString("00000000-0000-0000-0000-000000000015")});
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenPostMemberByBlokedUserThenReturn403() throws Exception {
        WrapperDto<UUID[]> dto = new WrapperDto<>();
        dto.setValue(new UUID[]{UUID.fromString("00000000-0000-0000-0000-000000000015")});
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteMemberByOwnerThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#2", roles = {"CLIENT"})
    public void whenDeleteMemberByUserThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#4", roles = {"CLIENT"})
    public void whenDeleteMemberByBlockedUserThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000001/members/00000000-0000-0000-0000-000000000003"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteMemberByModeratorThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000005/members/00000000-0000-0000-0000-000000000002"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenPostModeratorThenReturn200() throws Exception {
        IdDto dto = new IdDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000014"));
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenDeleteModeratorThenReturn204() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenPostModeratorByClientThenReturn403() throws Exception {
        IdDto dto = new IdDto();
        dto.setId(UUID.fromString("00000000-0000-0000-0000-000000000014"));
        String json = objectMapper.writeValueAsString(dto);
        mvc.perform(MockMvcRequestBuilders
                .post(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators"))
                .characterEncoding("utf-8")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "client#1", roles = {"CLIENT"})
    public void whenDeleteModeratorByClentThenReturn403() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete(PREFIX.concat("/00000000-0000-0000-0000-000000000002/moderators/00000000-0000-0000-0000-000000000005"))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}