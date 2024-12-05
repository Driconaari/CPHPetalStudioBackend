package com.flower.shop.cphpetalstudio.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Show all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manage-users")
    public String manageUsers() {
        return "admin/manage-users";  // Map to a Thymeleaf template that shows user management
    }

    // View all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view-orders")
    public String viewOrders() {
        return "admin/view-orders";  // Map to a Thymeleaf template for orders
    }

    // Manage inventory
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manage-inventory")
    public String manageInventory() {
        return "admin/manage-inventory";  // Map to a Thymeleaf template for inventory management
    }

    // Generate reports
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reports")
    public String generateReports() {
        return "admin/reports";  // Map to a Thymeleaf template for generating reports
    }
}
