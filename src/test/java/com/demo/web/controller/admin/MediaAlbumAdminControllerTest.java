package com.demo.web.controller.admin;

import com.demo.web.dto.admin.MediaAlbumRequest;
import com.demo.web.dto.admin.MediaItemRequest;
import com.demo.web.repository.MediaAlbumRepository;
import com.demo.web.repository.MediaItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MediaAlbumAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MediaAlbumRepository mediaAlbumRepository;

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @BeforeEach
    void setUp() {
        mediaItemRepository.deleteAll();
        mediaAlbumRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAlbumAndItem_shouldSucceed() throws Exception {
        MediaAlbumRequest albumRequest = new MediaAlbumRequest();
        albumRequest.setTitle("Showcase");
        albumRequest.setDescription("Album đặc biệt");

        String albumResponse = mockMvc.perform(post("/api/v1/admin/media/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(albumRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Showcase")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long albumId = objectMapper.readTree(albumResponse)
                .path("data")
                .path("id")
                .asLong();

        MediaItemRequest itemRequest = new MediaItemRequest();
        itemRequest.setTitle("Highlight");
        itemRequest.setType(com.demo.web.model.enums.MediaType.IMAGE);
        itemRequest.setUrl("https://example.com/highlight.jpg");
        itemRequest.setDisplayOrder(1);

        mockMvc.perform(post("/api/v1/admin/media/albums/{albumId}/items", albumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Highlight")));

        mockMvc.perform(get("/api/v1/admin/media/albums/{albumId}/items", albumId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void listAlbum_requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/admin/media/albums"))
                .andExpect(status().isUnauthorized());
    }
}
