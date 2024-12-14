package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.ApiResponse;
import com.flower.shop.cphpetalstudio.dto.CartItemDTO;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import com.flower.shop.cphpetalstudio.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:5500") // Allow frontend access
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final UserService userService;
    private final CartService cartService;
    private final BouquetService bouquetService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CartController(UserService userService, CartService cartService, BouquetService bouquetService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.cartService = cartService;
        this.bouquetService = bouquetService;
        this.jwtUtil = jwtUtil;
    }

    // Retrieve all cart items for the authenticated user jwt token local storage
    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItemsForUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<CartItem> cartItems = cartService.getCartItemsByUser(user);

            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(CartItemDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(cartItemDTOs);
        } catch (Exception e) {
            logger.error("Error retrieving cart items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add an item to the cart
    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        logger.info("Add to cart request received: {}", request);

        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());
            if (bouquet == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bouquet not found");
            }

            cartService.addToCart(user, bouquet, request.getQuantity());
            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            logger.error("Error adding item to cart: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item to cart");
        }
    }

    // Remove an item from the cart
    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        logger.info("Remove from cart request: Bouquet ID {}", request.getBouquetId());

        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            cartService.removeFromCart(user, request.getBouquetId());
            return ResponseEntity.ok("Item removed from cart");
        } catch (Exception e) {
            logger.error("Error removing item from cart: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove item from cart");
        }
    }

    // Retrieve cart count for the authenticated user
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        int cartCount = cartService.getCartCount(user);
        return ResponseEntity.ok(cartCount);
    }

    // Endpoint for order summary
    @GetMapping("/order-summary")
    public ResponseEntity<?> getOrderSummary(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            String username = authentication.getName();
            User user = userService.findByUsername(username);
            List<CartItem> cartItems = cartService.getCartItemsByUser(user);

            if (cartItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty.");
            }

            BigDecimal totalAmount = cartItems.stream()
                    .map(item -> item.getBouquet().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String orderId = "ORD" + System.currentTimeMillis();

            return ResponseEntity.ok(new OrderSummaryResponse(orderId, totalAmount));
        } catch (Exception e) {
            logger.error("Error generating order summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate order summary.");
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            String username = authentication.getName();
            User user = userService.findByUsername(username);

            cartService.clearCart(user.getId());
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully"));
        } catch (Exception e) {
            logger.error("Error clearing cart: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to clear cart.");
        }
    }


    // Helper class for Order Summary Response
    private static class OrderSummaryResponse {
        private final String orderId;
        private final BigDecimal totalAmount;

        public OrderSummaryResponse(String orderId, BigDecimal totalAmount) {
            this.orderId = orderId;
            this.totalAmount = totalAmount;
        }

        public String getOrderId() {
            return orderId;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }
    }
}
