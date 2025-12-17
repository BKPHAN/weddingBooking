package com.demo.web.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    @GetMapping
    public String rootRedirect() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.ui.Model model) {
        model.addAttribute("currentPage", "dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/content")
    public String contentManagement() {
        return "admin/content";
    }
}
