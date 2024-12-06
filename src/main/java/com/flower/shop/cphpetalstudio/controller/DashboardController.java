package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final BouquetService bouquetService;

    @Autowired
    public DashboardController(UserService userService, BouquetService bouquetService) {
        this.userService = userService;
        this.bouquetService = bouquetService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Bouquet> featuredBouquets = bouquetService.getFeaturedBouquets();
        model.addAttribute("user", user);
        model.addAttribute("bouquets", featuredBouquets);
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                ? "admin-dashboard" : "user-dashboard";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        List<Bouquet> allBouquets = bouquetService.getAllBouquets();
        model.addAttribute("bouquets", allBouquets);
        return "shop";
    }

    @GetMapping("/cart")
    public String cart(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        // Add cart logic here
        return "cart";
    }

    @GetMapping("/account/edit")
    public String editProfile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/account/edit")
    public String updateProfile(@RequestParam String email, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        userService.updateProfile(user, email);
        return "redirect:/dashboard";
    }

    @GetMapping("/account/change-password")
    public String changePasswordForm() {
        return "change-password";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        if (userService.changePassword(user, oldPassword, newPassword)) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/account/change-password?error";
        }
    }
}