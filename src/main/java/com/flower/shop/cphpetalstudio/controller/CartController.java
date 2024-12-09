package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
/*
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addBouquetToCart(@RequestBody CartItem cartItem, Authentication authentication) {
        try {
            String username = authentication.getName();
            CartItem addedItem = cartService.addBouquetToCart(username, cartItem);
            return ResponseEntity.ok(addedItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add bouquet to cart: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeBouquetFromCart(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.removeBouquetFromCart(username, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to remove bouquet from cart: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<CartItem> cartItems = cartService.getCartForUser(username);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve cart: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItem, Authentication authentication) {
        try {
            String username = authentication.getName();
            CartItem updatedItem = cartService.updateCartItem(username, id, cartItem);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update cart item: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.clearCart(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to clear cart: " + e.getMessage());
        }
    }


}




 */
