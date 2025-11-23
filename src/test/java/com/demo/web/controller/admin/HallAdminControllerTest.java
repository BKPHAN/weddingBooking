package com.demo.web.controller.admin;

import com.demo.web.dto.admin.HallRequest;
import com.demo.web.model.Hall;
import com.demo.web.repository.HallRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HallAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HallRepository hallRepository;

    @BeforeEach
    void setUp() {
        hallRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createHall_shouldReturnPersistedHall() throws Exception {
        HallRequest request = new HallRequest();
        request.setCode("PEARL");
        request.setName("Pearl Hall");
        request.setCapacity(320);
        request.setBasePrice(BigDecimal.valueOf(110_000_000));
        request.setAmenities("[\"LED Wall\"]");

        mockMvc.perform(post("/api/v1/admin/halls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code", is("PEARL")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listHall_shouldBeAccessibleForAdmin() throws Exception {
        Hall hall = new Hall();
        hall.setCode("SAPPHIRE");
        hall.setName("Sapphire Hall");
        hall.setCapacity(200);
        hall.setBasePrice(BigDecimal.valueOf(80000000));
        hallRepository.save(hall);

        mockMvc.perform(get("/api/v1/admin/halls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("SAPPHIRE")));
    }

    @Test
    void listHall_shouldReturnUnauthorizedForAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/admin/halls"))
                .andExpect(status().isUnauthorized());
    }
}
