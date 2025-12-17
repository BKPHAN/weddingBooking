package com.demo.web.controller.admin;

import com.demo.web.model.Menu;
import com.demo.web.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/menus")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMenuController {

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public String listMenus(Model model) {
        model.addAttribute("currentPage", "menus");
        model.addAttribute("menus", menuService.findAll());
        return "admin/menus";
    }

    @PostMapping("/save")
    public String saveMenu(@ModelAttribute Menu menu, RedirectAttributes redirectAttributes) {
        try {
            // Ensure boolean fields are handled if necessary
            menuService.saveMenu(menu);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu món ăn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu món ăn: " + e.getMessage());
        }
        return "redirect:/admin/menus";
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa món ăn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa món ăn: " + e.getMessage());
        }
        return "redirect:/admin/menus";
    }
}
