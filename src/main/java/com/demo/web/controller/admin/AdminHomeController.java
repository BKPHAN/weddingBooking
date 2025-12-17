package com.demo.web.controller.admin;

import com.demo.web.model.MediaItem;
import com.demo.web.model.enums.MediaType;
import com.demo.web.service.MediaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/home")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHomeController {

    private final MediaService mediaService;

    public AdminHomeController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public String homeConfig(Model model) {
        model.addAttribute("currentPage", "home");
        model.addAttribute("sliders", mediaService.findAllSliders());
        return "admin/home";
    }

    @PostMapping("/slider/save")
    public String saveSlider(@ModelAttribute MediaItem slider, RedirectAttributes redirectAttributes) {
        try {
            slider.setType(MediaType.SLIDER);
            mediaService.saveMediaItem(slider);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/home";
    }

    @PostMapping("/slider/delete/{id}")
    public String deleteSlider(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteMediaItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa banner thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/home";
    }
}
