package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import com.flower.shop.cphpetalstudio.entity.*;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final BouquetService bouquetService;

    @Autowired
    public CartController(UserService userService, CartService cartService, CartItemRepository cartItemRepository, BouquetService bouquetService) {
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.bouquetService = bouquetService;
    }

    @PostMapping("/add-to-cart")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        logger.info("Add to cart request received: {}", request);

        try {
            // Verify user authentication
            String username = authentication.getName();
            logger.info("Authenticated user: {}", username);
            User user = userService.findByUsername(username);

            // Verify bouquet existence
            Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());
            if (bouquet == null) {
                logger.warn("Bouquet with ID {} not found", request.getBouquetId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bouquet not found");
            }

            // Add to cart
            cartService.addToCart(user, bouquet, request.getQuantity());
            logger.info("Item added to cart: Bouquet ID {}, Quantity {}", request.getBouquetId(), request.getQuantity());

            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            logger.error("Error adding item to cart: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item to cart");
        }
    }


    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        logger.info("Removing bouquet from cart - bouquetId: {}", request.getBouquetId());

        try {
            User user = userService.findByUsername(authentication.getName());
            // Pass bouquetId (Long) instead of the entire Bouquet object
            cartService.removeFromCart(user, request.getBouquetId());
            return ResponseEntity.ok("Item removed from cart");
        } catch (Exception e) {
            logger.error("Error removing from cart: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove from cart");
        }
    }


    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestParam Long userId) {
        List<CartItem> cartItems = cartService.getCartByUser(userId);  // Correct usage
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);  // Correct usage
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        int count = cartItemRepository.countByUser(user);
        return ResponseEntity.ok(count);
    }
}