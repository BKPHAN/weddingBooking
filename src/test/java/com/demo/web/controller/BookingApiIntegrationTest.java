package com.demo.web.controller;

import com.demo.web.dto.BookingRequest;
import com.demo.web.model.Hall;
import com.demo.web.repository.BookingRepository;
import com.demo.web.repository.HallRepository;
import com.demo.web.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HallRepository hallRepository;

    @MockBean
    private NotificationService notificationService;

    private Hall hall;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        hallRepository.deleteAll();

        hall = new Hall();
        hall.setCode("HALL-001");
        hall.setName("Emerald Hall");
        hall.setCapacity(400);
        hall.setBasePrice(BigDecimal.valueOf(120_000_000));
        hallRepository.save(hall);
    }

    @Test
    void createBooking_shouldReturnSummary() throws Exception {
        BookingRequest request = buildRequest();
        request.setHallId(hall.getId());

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.hallName", is("Emerald Hall")))
                .andExpect(jsonPath("$.data.services[0]", containsString("PHOTOGRAPHY")));
    }

    @Test
    void createBooking_shouldRejectDuplicatePerEmailAndDate() throws Exception {
        BookingRequest request = buildRequest();

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        reset(notificationService);

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("đã gửi yêu cầu")));

        verifyNoInteractions(notificationService);
    }

    @Test
    void createBooking_shouldRejectWhenHallConflict() throws Exception {
        BookingRequest request = buildRequest();
        request.setHallId(hall.getId());

        // first booking occupies slot
        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        reset(notificationService);

        BookingRequest conflict = buildRequest();
        conflict.setEmail("other@example.com");
        conflict.setHallId(hall.getId());

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflict)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Khung giờ này của sảnh đã có khách")));
    }

    private BookingRequest buildRequest() {
        BookingRequest request = new BookingRequest();
        request.setBrideName("Lan");
        request.setGroomName("Minh");
        request.setEmail("lan@example.com");
        request.setPhone("0987654321");
        request.setEventDate(LocalDate.now().plusDays(7));
        request.setTimeSlot("EVENING");
        request.setGuestCount(200);
        request.setBudgetMin(BigDecimal.valueOf(100_000_000));
        request.setBudgetMax(BigDecimal.valueOf(150_000_000));
        request.setServices(Set.of("PHOTOGRAPHY"));
        request.setNotes("Trang trí hoa tươi");
        return request;
    }
}
