package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/user")
public class UserController {


    public UserController(UserService userService) {
        this.userService = userService;
    }



    @Autowired
    private UserService userService;


    @GetMapping("/check-role")
    public ResponseEntity<String> checkUserRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok("User is not authenticated");
        }

        String username = authentication.getName();
        boolean isAdmin = userService.isUserAdmin(username);

        return ResponseEntity.ok("User " + username + " is " + (isAdmin ? "an admin" : "not an admin"));
    }
}