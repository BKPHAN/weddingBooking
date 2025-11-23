package com.demo.web.controller;

import com.demo.web.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.demo.web.controller")
public class GlobalModelAttributes {

    @ModelAttribute("currentUserDisplayName")
    public String populateDisplayName(Authentication authentication) {
        if (authentication == null) {
            return "Quản trị viên";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            String fullName = userPrincipal.getFullName();
            if (fullName != null && !fullName.isBlank()) {
                return fullName;
            }
        }

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            if (username != null && !username.isBlank()) {
                return username;
            }
        }

        String authName = authentication.getName();
        return authName != null && !authName.isBlank() ? authName : "Quản trị viên";
    }
}
