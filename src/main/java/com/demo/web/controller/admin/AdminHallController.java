package com.demo.web.controller.admin;

import com.demo.web.model.Hall;
import com.demo.web.service.HallService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/halls")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHallController {

    private final HallService hallService;

    public AdminHallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public String listHalls(Model model) {
        model.addAttribute("currentPage", "halls");
        model.addAttribute("halls", hallService.findAll());
        return "admin/halls";
    }

    @PostMapping("/save")
    public String saveHall(@ModelAttribute Hall hall, RedirectAttributes redirectAttributes) {
        try {
            hallService.saveHall(hall);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu thông tin sảnh thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu sảnh: " + e.getMessage());
        }
        return "redirect:/admin/halls";
    }

    @PostMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            hallService.deleteHall(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sảnh thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sảnh: " + e.getMessage());
        }
        return "redirect:/admin/halls";
    }
}
