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

    @GetMapping("/payment")
    public String showPaymentPage() {
        return "payment"; // This should correspond to payment.html in your templates directory
    }

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public String addBouquetToCart(@RequestBody CartItem cartItem) {
        cartService.addBouquetToCart(cartItem);
        // Redirect to the payment page after adding to cart
        return "/payment";
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
