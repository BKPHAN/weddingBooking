package com.demo.web.controller.admin;

import com.demo.web.model.MediaAlbum;
import com.demo.web.service.MediaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/gallery")
@PreAuthorize("hasRole('ADMIN')")
public class AdminGalleryController {

    private final MediaService mediaService;

    public AdminGalleryController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public String listAlbums(Model model) {
        model.addAttribute("currentPage", "gallery");
        model.addAttribute("albums", mediaService.findAllAlbums());
        return "admin/gallery";
    }

    @PostMapping("/save")
    public String saveAlbum(@ModelAttribute MediaAlbum album, RedirectAttributes redirectAttributes) {
        try {
            mediaService.saveAlbum(album);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu album thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu album: " + e.getMessage());
        }
        return "redirect:/admin/gallery";
    }

    @PostMapping("/delete/{id}")
    public String deleteAlbum(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteAlbum(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa album thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa album: " + e.getMessage());
        }
        return "redirect:/admin/gallery";
    }
}
