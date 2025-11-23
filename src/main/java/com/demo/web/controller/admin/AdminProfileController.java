package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.UserProfileResponse;
import com.demo.web.security.UserPrincipal;
import com.demo.web.service.admin.AdminProfileService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/profile")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    public AdminProfileController(AdminProfileService adminProfileService) {
        this.adminProfileService = adminProfileService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentProfile(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        UserProfileResponse profile = adminProfileService.getProfile(principal.getId());
        return new ApiResponse<>(true, "Thông tin cá nhân", profile);
    }
}
