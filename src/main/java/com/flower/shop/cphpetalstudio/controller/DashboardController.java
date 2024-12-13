package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin

public class DashboardController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        logger.info("Fetched user: {}", user);
        model.addAttribute("user", user);

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin-dashboard";
        } else {
            return "user-dashboard";
        }
    }

    @GetMapping("/account/edit")
    public String editProfile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @GetMapping("/account/change-password")
    public String changePassword() {
        return "change-password";
    }

    @PostMapping("/account/edit")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());
        userService.saveUser(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Profile updated successfully");
        return "edit-profile";
    }

    @PostMapping("/account/change-password")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        if (userService.checkPassword(currentUser, oldPassword)) {
            userService.updatePassword(currentUser, newPassword);
            model.addAttribute("message", "Password updated successfully");
        } else {
            model.addAttribute("error", "Old password is incorrect");
        }
        return "change-password";
    }
}