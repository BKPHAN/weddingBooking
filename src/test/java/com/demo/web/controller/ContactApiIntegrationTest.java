package com.demo.web.controller;

import com.demo.web.dto.ContactRequest;
import com.demo.web.repository.ContactRepository;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    @MockBean
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
    }

    @Test
    void saveContact_shouldMarkUrgentAndReturnResponse() throws Exception {
        ContactRequest request = new ContactRequest();
        request.setFullName("Trần Văn B");
        request.setEmail("tvb@example.com");
        request.setPhone("0912345678");
        request.setSubject("Tư vấn gấp");
        request.setMessage("Tôi cần tư vấn gấp trong hôm nay.");

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.flag", is("URGENT")));
    }

    @Test
    void saveContact_shouldReturnFollowUpForRepeatEmail() throws Exception {
        ContactRequest initial = new ContactRequest();
        initial.setFullName("Lan");
        initial.setEmail("lan@example.com");
        initial.setPhone("0900000000");
        initial.setSubject("Booking");
        initial.setMessage("Xin gửi báo giá.");

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initial)))
                .andExpect(status().isOk());

        ContactRequest followUp = new ContactRequest();
        followUp.setFullName("Lan");
        followUp.setEmail("lan@example.com");
        followUp.setPhone("0900000000");
        followUp.setSubject("Nhắc lại");
        followUp.setMessage("Tôi cần thông tin thêm.");

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followUp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.flag", is("FOLLOW_UP")));
    }
}
