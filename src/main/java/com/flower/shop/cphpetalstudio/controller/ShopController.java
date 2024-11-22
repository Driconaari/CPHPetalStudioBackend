package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    private BouquetService bouquetService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/bouquets/{id}")
    public Bouquet getBouquet(@PathVariable Long id) {
        return bouquetService.getBouquetById(id);
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody List<Bouquet> bouquets, Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return orderService.createOrder(user, bouquets);
    }
}