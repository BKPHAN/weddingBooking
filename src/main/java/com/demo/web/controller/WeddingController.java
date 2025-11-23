package com.demo.web.controller;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.BookingRequest;
import com.demo.web.dto.BookingResponse;
import com.demo.web.dto.ContactRequest;
import com.demo.web.dto.ContactResponse;
import com.demo.web.mapper.BookingMapper;
import com.demo.web.mapper.ContactMapper;
import com.demo.web.service.BookingService;
import com.demo.web.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WeddingController {

    private final BookingService bookingService;
    private final ContactService contactService;
    private final BookingMapper bookingMapper;
    private final ContactMapper contactMapper;

    public WeddingController(BookingService bookingService,
                             ContactService contactService,
                             BookingMapper bookingMapper,
                             ContactMapper contactMapper) {
        this.bookingService = bookingService;
        this.contactService = contactService;
        this.bookingMapper = bookingMapper;
        this.contactMapper = contactMapper;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/wedding")
    public String homePage() {
        return "home";
    }

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        model.addAttribute("bookingRequest", new BookingRequest());
        return "booking";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage() {
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("contactRequest", new ContactRequest());
        return "contact";
    }

    @PostMapping("/api/booking")
    @ResponseBody
    public ResponseEntity<ApiResponse<BookingResponse>> saveBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        var saved = bookingService.createBooking(bookingMapper.toEntity(bookingRequest),
                bookingRequest.getHallId(),
                bookingRequest.getServices());
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Đặt tiệc thành công! Chúng tôi sẽ liên hệ để xác nhận chi tiết.",
                bookingMapper.toResponse(saved)
        ));
    }

    @PostMapping("/api/feedbacks")
    @ResponseBody
    public ResponseEntity<ApiResponse<ContactResponse>> saveContact(@Valid @RequestBody ContactRequest contactRequest) {
        var saved = contactService.saveContact(contactMapper.toEntity(contactRequest));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cảm ơn bạn đã liên hệ! Đội ngũ tư vấn sẽ phản hồi trong thời gian sớm nhất.",
                contactMapper.toResponse(saved)
        ));
    }

    @GetMapping("/api/feedbacks")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getLatestContacts() {
        List<ContactResponse> contacts = contactMapper.toResponseList(contactService.getLatestContacts());
        return ResponseEntity.ok(new ApiResponse<>(true, "Latest feedbacks", contacts));
    }
}
