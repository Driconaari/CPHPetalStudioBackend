package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final BouquetService bouquetService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public ShopController(BouquetService bouquetService, OrderService orderService, UserService userService) {
        this.bouquetService = bouquetService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/bouquets/{id}")
    public ResponseEntity<?> getBouquet(@PathVariable Long id) {
        try {
            Bouquet bouquet = bouquetService.getBouquetById(id);
            return ResponseEntity.ok(bouquet);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody List<Bouquet> bouquets, Authentication authentication) {
        if (bouquets == null || bouquets.isEmpty()) {
            return ResponseEntity.badRequest().body("Order must contain at least one bouquet");
        }
        try {
            User user = userService.findByUsername(authentication.getName());
            Order order = orderService.createOrder(user, bouquets);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while creating the order: " + e.getMessage());
        }
    }
}