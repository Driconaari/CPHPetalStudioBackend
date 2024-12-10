package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.ApiResponse;
import com.flower.shop.cphpetalstudio.dto.UpdateCartItemRequest;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // Get cart items for logged-in user
    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(Principal principal) {
        User user = userService.getLoggedInUser();
        List<CartItem> cartItems = cartService.getCartItemsForUser(user);
        return ResponseEntity.ok(cartItems);
    }

    // Add bouquet to cart
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

    // Remove bouquet from cart
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

    // Update cart item quantity
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

    // Clear cart for the user
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

    // Get cart item count for the user
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        String username = authentication.getName();
        int count = cartService.getCartItemCount(username);
        return ResponseEntity.ok(count);
    }

    // Get cart item count (for user logged in via Principal)
    @GetMapping("/cart/count")
    public ResponseEntity<Integer> getCartCountForUser(Principal principal) {
        User loggedInUser = userService.getLoggedInUser();
        int count = cartService.getCartCount(loggedInUser);
        return ResponseEntity.ok(count);
    }
}
