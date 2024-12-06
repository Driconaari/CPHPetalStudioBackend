package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-role")
    public ResponseEntity<String> checkUserRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok("User is not authenticated");
        }

        String username = authentication.getName();
        boolean isAdmin = userService.isUserAdmin(username);

        return ResponseEntity.ok("User " + username + " is " + (isAdmin ? "an admin" : "not an admin"));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate email format
        if (!isValidEmail(updatedUser.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        // Check if the new username is already taken (if changed)
        if (!currentUser.getUsername().equals(updatedUser.getUsername()) &&
                userService.findByUsername(updatedUser.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Update user details
        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());
        User savedUser = userService.saveUser(currentUser);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword,
                                                 @RequestParam String newPassword,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!userService.checkPassword(user, oldPassword)) {
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }

        if (!isValidPassword(newPassword)) {
            return ResponseEntity.badRequest().body("New password does not meet security requirements.");
        }

        userService.updatePassword(user, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long and contain at least one digit, one lowercase, one uppercase letter
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return password.matches(passwordRegex);
    }
}