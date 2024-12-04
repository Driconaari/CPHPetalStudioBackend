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
import org.springframework.web.bind.annotation.GetMapping;

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

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin-dashboard";
        } else {
            return "user-dashboard";
        }
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
        // Assuming you have a method to get user's cart items
        // List<CartItem> cartItems = cartService.getCartItemsForUser(user);
        // model.addAttribute("cartItems", cartItems);
        return "cart";
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
}