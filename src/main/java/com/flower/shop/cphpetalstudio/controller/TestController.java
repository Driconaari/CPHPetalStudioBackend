package com.flower.shop.cphpetalstudio.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/user")
    public String userAccess(Authentication authentication) {
        return "User Content. Welcome, " + authentication.getName() + "!";
    }

    @GetMapping("/admin")
    public String adminAccess(Authentication authentication) {
        return "Admin Content. Welcome, " + authentication.getName() + "!";
    }
}