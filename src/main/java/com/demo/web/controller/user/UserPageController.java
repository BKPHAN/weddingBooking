package com.demo.web.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserPageController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "user/dashboard";
    }
}
