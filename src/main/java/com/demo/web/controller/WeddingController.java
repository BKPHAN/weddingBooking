package com.demo.web.controller;

import com.demo.web.model.Booking;
import com.demo.web.model.Contact;
import com.demo.web.model.User;
import com.demo.web.service.BookingService;
import com.demo.web.service.ContactService;
import com.demo.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Controller
public class WeddingController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    @Autowired
    private ContactService contactService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping({"/wedding"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        model.addAttribute("wedding", new Booking());
        return "booking";  // Chuyển tới trang bookingForm.html
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";  // Chuyển tới trang about.html
    }

    @GetMapping("/services")
    public String servicesPage() {
        return "services";  // Chuyển tới trang services.html
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";  // Chuyển tới trang contact.html
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request) {
        try {
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("Registration successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST method để nhận và lưu thông tin liên hệ, chỉ trả về true/false
    @PostMapping("/api/feedbacks")
    public ResponseEntity<Object> saveContact(@RequestBody Contact contact) {
        try {
            // Thiết lập createDate, updateDate và flag
            contact.setCreateDate(new Date());
            contact.setUpdateDate(new Date());  // Đặt bằng createDate lần đầu
            contact.setFlag("0");  // Gán flag = 0
            // Lưu thông tin liên hệ và kiểm tra nếu lưu thành công
            boolean isSaved = contactService.saveContact(contact);

            if (isSaved) {
                return ResponseEntity.ok(true);  // Trả về true nếu lưu thành công
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);  // Trả về false nếu lưu không thành công
            }
        } catch (Exception e) {
            // Xử lý lỗi khi lưu thông tin liên hệ
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);  // Trả về false nếu có lỗi
        }
    }

    // GET method để lấy danh sách 10 phản hồi mới nhất
    @GetMapping("/api/feedbacks")
    public ResponseEntity<List<Contact>> getLatestContacts() {
        try {
            // Lấy 10 phản hồi mới nhất
            List<Contact> latestContacts = contactService.getLatestContacts();
            return ResponseEntity.ok(latestContacts);  // Trả về danh sách các phản hồi mới nhất
        } catch (Exception e) {
            // Xử lý lỗi khi lấy danh sách phản hồi
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Trả về lỗi nếu có sự cố
        }
    }

    // POST method để nhận và lưu thông tin đặt tiệc, chỉ trả về true/false
    @PostMapping("/api/booking")
    public ResponseEntity<Object> saveBooking(@RequestBody Booking booking) {
        try {
            // Thiết lập createDate và updateDate cho bản ghi booking
            booking.setCreateDate(new Date());
            booking.setUpdateDate(new Date());  // Đặt bằng createDate lần đầu
            booking.setFlag("0");  // Gán trạng thái ban đầu là "PENDING"

            // Lưu thông tin đặt tiệc và kiểm tra nếu lưu thành công
            boolean isSaved = bookingService.saveBooking(booking);

            if (isSaved) {
                return ResponseEntity.ok(true);  // Trả về true nếu lưu thành công
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);  // Trả về false nếu lưu không thành công
            }
        } catch (Exception e) {
            // Xử lý lỗi khi lưu thông tin đặt tiệc
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);  // Trả về false nếu có lỗi
        }
    }
}
