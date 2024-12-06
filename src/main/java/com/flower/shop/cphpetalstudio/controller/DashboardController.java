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

    // This method handles both user and admin dashboard rendering.
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Fetching the user details based on the username in the authentication object (which is extracted from the JWT)
        User user = userService.findByUsername(authentication.getName());

        // Get featured bouquets (you can modify this depending on your business logic)
        List<Bouquet> featuredBouquets = bouquetService.getFeaturedBouquets();

        // Add the user and featured bouquets to the model, so they can be accessed in the Thymeleaf template
        model.addAttribute("user", user);
        model.addAttribute("bouquets", featuredBouquets);

        // Check the user's role and return the appropriate dashboard page
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin-dashboard"; // Admin dashboard template
        } else {
            return "user-dashboard"; // User dashboard template
        }
    }

    // This handles displaying all bouquets in the shop page
    @GetMapping("/shop")
    public String shop(Model model) {
        // Get all bouquets and pass them to the model to display in the shop page
        List<Bouquet> allBouquets = bouquetService.getAllBouquets();
        model.addAttribute("bouquets", allBouquets);
        return "shop"; // Shop page template
    }

    // This handles the user's cart page
    @GetMapping("/cart")
    public String cart(Model model, Authentication authentication) {
        // Fetch the user based on the authentication token (JWT) provided
        User user = userService.findByUsername(authentication.getName());

        // Assuming you have a cartService to fetch the user's cart items
        // List<CartItem> cartItems = cartService.getCartItemsForUser(user);
        // model.addAttribute("cartItems", cartItems);

        // Render the cart page (you can add cart data here)
        return "cart"; // Cart page template
    }

    // This allows the user to edit their profile
    @GetMapping("/account/edit")
    public String editProfile(Model model, Authentication authentication) {
        // Fetch the user for the edit profile page
        User user = userService.findByUsername(authentication.getName());

        // Add the user object to the model for displaying in the edit profile page
        model.addAttribute("user", user);
        return "edit-profile"; // Edit profile page template
    }

    // This page allows the user to change their password
    @GetMapping("/account/change-password")
    public String changePassword() {
        return "change-password"; // Change password page template
    }
}
