package com.demo.web.controller.admin;

import com.demo.web.model.enums.BookingStatus;
import com.demo.web.service.BookingService;
import com.demo.web.service.ContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final BookingService bookingService;
    private final ContactService contactService;

    public AdminBookingController(BookingService bookingService, ContactService contactService) {
        this.bookingService = bookingService;
        this.contactService = contactService;
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("currentPage", "bookings");
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("contacts", contactService.getAllContacts());
        return "admin/bookings";
    }

    @PostMapping("/booking/status/{id}")
    public String updateBookingStatus(@PathVariable Long id, @RequestParam BookingStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateBookingStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái đặt tiệc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/contact/resolve/{id}")
    public String resolveContact(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.markResolved(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã đánh dấu xử lý liên hệ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }
}
