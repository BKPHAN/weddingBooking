package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.DashboardResponse;
import com.demo.web.service.admin.AdminDashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/overview")
    public ApiResponse<DashboardResponse> overview() {
        return new ApiResponse<>(
                true,
                "Dashboard overview",
                adminDashboardService.getOverview()
        );
    }
}
