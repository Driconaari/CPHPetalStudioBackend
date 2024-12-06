package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public void addBouquetToCart(@RequestBody CartItem cartItem) {
        cartService.addBouquetToCart(cartItem);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/{id}")
    public void removeBouquetFromCart(@PathVariable Long id) {
        cartService.removeBouquetFromCart(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<CartItem> getCart() {
        return cartService.getCartForUser();
    }



}
