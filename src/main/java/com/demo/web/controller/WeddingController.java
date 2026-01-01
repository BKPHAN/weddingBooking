package com.demo.web.controller;

import com.demo.web.service.BookingService;
import com.demo.web.service.ContactService;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.BookingRequest;
import com.demo.web.dto.BookingResponse;
import com.demo.web.dto.ContactRequest;
import com.demo.web.dto.ContactResponse;
import com.demo.web.mapper.BookingMapper;
import com.demo.web.mapper.ContactMapper;
import com.demo.web.service.HallService;
import com.demo.web.service.MediaService;
import com.demo.web.service.MenuService;
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
    private final HallService hallService;
    private final MediaService mediaService;
    private final MenuService menuService;
    private final BookingMapper bookingMapper;
    private final ContactMapper contactMapper;

    public WeddingController(BookingService bookingService,
            ContactService contactService,
            HallService hallService,
            MediaService mediaService,
            MenuService menuService,
            BookingMapper bookingMapper,
            ContactMapper contactMapper) {
        this.bookingService = bookingService;
        this.contactService = contactService;
        this.hallService = hallService;
        this.mediaService = mediaService;
        this.menuService = menuService;
        this.bookingMapper = bookingMapper;
        this.contactMapper = contactMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("featuredHalls", hallService.findFeaturedHalls());
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/?login=true";
    }

    @GetMapping("/wedding")
    public String homePage() {
        return "redirect:/";
    }

    @GetMapping("/booking")
    public String showBookingForm(@org.springframework.web.bind.annotation.RequestParam(required = false) Long hallId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) java.time.LocalDate date,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String guests,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String type,
            Model model) {
        BookingRequest request = new BookingRequest();
        if (hallId != null) {
            request.setHallId(hallId);
        }
        if (date != null) {
            request.setEventDate(date);
        }
        if (guests != null && !guests.isEmpty()) {
            try {
                // Extract first number from string like "100-300" or "500+"
                String numberOnly = guests.replaceAll("[^0-9]", " ").trim().split("\\s+")[0];
                request.setGuestCount(Integer.parseInt(numberOnly));
            } catch (Exception e) {
                // Ignore parse error
            }
        }
        if (type != null && !type.isEmpty()) {
            String typeLabel = switch (type) {
                case "wedding" -> "Tiệc Cưới";
                case "event" -> "Sự Kiện";
                case "conference" -> "Hội Nghị";
                default -> type;
            };
            request.setNotes("Loại hình: " + typeLabel);
        }

        model.addAttribute("bookingRequest", request);
        return "booking";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/services")
    public String services(Model model) {
        // Load service items from menus table with type='SERVICE'
        var serviceItems = menuService.findAll().stream()
                .filter(menu -> "SERVICE".equals(menu.getType()))
                .toList();

        model.addAttribute("serviceItems", serviceItems);
        return "services";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("menuItems", menuService.findAll());
        return "menu";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        // Load gallery items from menus table with type='GALLERY'
        var galleryItems = menuService.findAll().stream()
                .filter(menu -> "GALLERY".equals(menu.getType()))
                .toList();

        model.addAttribute("galleryItems", galleryItems);
        return "gallery";
    }

    @GetMapping("/collection")
    public String collection(Model model) {
        // Load all albums with their media items
        var albums = mediaService.findAllAlbums();

        // For each album, load its media items
        var albumsWithItems = albums.stream()
                .map(album -> {
                    var items = mediaService.findItemsByAlbumId(album.getId());
                    var albumData = new java.util.HashMap<String, Object>();
                    albumData.put("album", album);
                    albumData.put("items", items);
                    return albumData;
                })
                .toList();

        model.addAttribute("albumsWithItems", albumsWithItems);
        return "collection";
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
                bookingMapper.toResponse(saved)));
    }

    @PostMapping("/api/feedbacks")
    @ResponseBody
    public ResponseEntity<ApiResponse<ContactResponse>> saveContact(@Valid @RequestBody ContactRequest contactRequest) {
        var saved = contactService.saveContact(contactMapper.toEntity(contactRequest));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cảm ơn bạn đã liên hệ! Đội ngũ tư vấn sẽ phản hồi trong thời gian sớm nhất.",
                contactMapper.toResponse(saved)));
    }

    @GetMapping("/api/feedbacks")
    @ResponseBody
    public ResponseEntity<ApiResponse<List<ContactResponse>>> getLatestContacts() {
        List<ContactResponse> contacts = contactMapper.toResponseList(contactService.getLatestContacts());
        return ResponseEntity.ok(new ApiResponse<>(true, "Latest feedbacks", contacts));
    }
}
