package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.ApiResponse;
import com.flower.shop.cphpetalstudio.dto.UpdateCartItemRequest;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBouquetToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.addBouquetToCart(username, request.getBouquetId(), request.getQuantity());
            return ResponseEntity.ok(new ApiResponse("Item added to cart successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to add bouquet to cart: " + e.getMessage(), false));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse> removeBouquetFromCart(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.removeBouquetFromCart(username, id);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to remove bouquet from cart: " + e.getMessage(), false));
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
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to retrieve cart: " + e.getMessage(), false));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long id, @RequestBody UpdateCartItemRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.updateCartItem(username, id, request.getQuantity());
            return ResponseEntity.ok(new ApiResponse("Cart item updated successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to update cart item: " + e.getMessage(), false));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart(Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.clearCart(username);
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to clear cart: " + e.getMessage(), false));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        String username = authentication.getName();
        int count = cartService.getCartItemCount(username);
        return ResponseEntity.ok(count);
    }
}
