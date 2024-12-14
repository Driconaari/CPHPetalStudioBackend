package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.PaymentRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.SubscriptionService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final SubscriptionService subscriptionService;
    private final OrderService orderService;
    private final UserService userService;
    private final BouquetService bouquetService;
    private final CartService cartService;

    @Autowired
    public PaymentController(SubscriptionService subscriptionService, OrderService orderService, UserService userService,
                             BouquetService bouquetService, CartService cartService) {
        this.subscriptionService = subscriptionService;
        this.orderService = orderService;
        this.userService = userService;
        this.bouquetService = bouquetService;
        this.cartService = cartService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Fetch the current user securely
            User user = userService.getCurrentUser();

            if (paymentRequest.isSubscription()) {
                paymentRequest.setUser(user); // Ensure user info is set in the PaymentRequest
                subscriptionService.createSubscription(paymentRequest);
                return ResponseEntity.ok("Subscription created successfully.");
            } else {
                // Fetch bouquet and create order
                Bouquet bouquet = bouquetService.getBouquetById(paymentRequest.getBouquetId());
                List<Bouquet> bouquets = List.of(bouquet);
                orderService.createOrder(user, bouquets);
                return ResponseEntity.ok("Order placed successfully.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during checkout: " + e.getMessage());
        }
    }

    @GetMapping("/order-summary")
    public ResponseEntity<?> getOrderSummary() {
        try {
            // Assuming you already have a way to fetch the current authenticated user
            User user = userService.getCurrentUser(); // Fetch the current user using your existing logic
            List<CartItem> cartItems = cartService.getCartItemsByUser(user);

            // Calculate total amount and generate an order ID
            BigDecimal totalAmount = cartItems.stream()
                    .map(item -> item.getBouquet().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String orderId = "ORD" + System.currentTimeMillis(); // Generate a simple order ID

            // Return the response with the order summary
            return ResponseEntity.ok(new OrderSummaryResponse(orderId, totalAmount));
        } catch (Exception e) {
            // Return an error response in case of failure
            return ResponseEntity.status(500).body("Failed to generate order summary.");
        }
    }

    // Helper class for the order summary response
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
