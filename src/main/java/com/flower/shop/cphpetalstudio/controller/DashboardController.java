package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin-dashboard";
        } else {
            return "user-dashboard";
        }
    }
}