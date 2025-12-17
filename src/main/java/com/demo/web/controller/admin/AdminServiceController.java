package com.demo.web.controller.admin;

import com.demo.web.model.Promotion;
import com.demo.web.service.PromotionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/services")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceController {

    private final PromotionService promotionService;

    public AdminServiceController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public String listServices(Model model) {
        model.addAttribute("currentPage", "services");
        model.addAttribute("services", promotionService.findAll());
        return "admin/services";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute Promotion promotion, RedirectAttributes redirectAttributes) {
        try {
            promotionService.savePromotion(promotion);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu dịch vụ/ưu đãi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/services";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/services";
    }
}
