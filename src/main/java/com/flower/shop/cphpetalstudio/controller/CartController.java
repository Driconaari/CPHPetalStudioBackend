package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.ApiError;
import com.flower.shop.cphpetalstudio.dto.ApiResponse;
import com.flower.shop.cphpetalstudio.dto.UpdateCartItemRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final UserService userService;
    private final BouquetService bouquetService;

    @Autowired
    public CartController(CartService cartService, UserService userService, BouquetService bouquetService) {
        this.cartService = cartService;
        this.userService = userService;
        this.bouquetService = bouquetService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addBouquetToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        try {
            // Get the username from the authentication object
            String username = authentication.getName();
            logger.info("Adding bouquet to cart for user: {}", username);

            // Find the user from the username
            User user = userService.findByUsername(username);

            // Get the bouquet by its ID
            Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());

            // Add the bouquet to the user's cart
            CartItem addedItem = cartService.addBouquetToCart(user.getUsername(), bouquet.getId(), request.getQuantity());
            logger.info("Bouquet added to cart: {}", addedItem);
            return ResponseEntity.ok(addedItem);

        } catch (Exception e) {
            logger.error("Failed to add bouquet to cart", e);
            return ResponseEntity.badRequest().body(new ApiError("Failed to add bouquet to cart", e.getMessage()));
        }
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeBouquetFromCart(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Removing bouquet from cart for user: {}", username);
            cartService.removeBouquetFromCart(username, id);
            logger.info("Bouquet removed from cart: {}", id);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully"));
        } catch (Exception e) {
            logger.error("Failed to remove bouquet from cart", e);
            return ResponseEntity.badRequest().body(new ApiError("Failed to remove bouquet from cart", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Fetching cart for user: {}", username);
            List<CartItem> cartItems = cartService.getCartForUser(username);
            logger.info("Cart items retrieved: {}", cartItems);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            logger.error("Failed to retrieve cart", e);
            return ResponseEntity.badRequest().body(new ApiError("Failed to retrieve cart", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestBody UpdateCartItemRequest request, Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Updating cart item for user: {}", username);
            CartItem updatedItem = cartService.updateCartItem(username, id, request.getQuantity());
            logger.info("Cart item updated: {}", updatedItem);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            logger.error("Failed to update cart item", e);
            return ResponseEntity.badRequest().body(new ApiError("Failed to update cart item", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Clearing cart for user: {}", username);
            cartService.clearCart(username);
            logger.info("Cart cleared for user: {}", username);
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully"));
        } catch (Exception e) {
            logger.error("Failed to clear cart", e);
            return ResponseEntity.badRequest().body(new ApiError("Failed to clear cart", e.getMessage()));
        }
    }
}