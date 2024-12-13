package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.PaymentRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.SubscriptionService;
import com.flower.shop.cphpetalstudio.service.UserService;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final SubscriptionService subscriptionService;
    private final OrderService orderService;
    private final UserService userService;
    private final BouquetService bouquetService;

    @Autowired
    public PaymentController(SubscriptionService subscriptionService, OrderService orderService, UserService userService, BouquetService bouquetService) {
        this.subscriptionService = subscriptionService;
        this.orderService = orderService;
        this.userService = userService;
        this.bouquetService = bouquetService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody PaymentRequest paymentRequest) {
        try {
            if (paymentRequest.isSubscription()) {
                subscriptionService.createSubscription(paymentRequest);
                return ResponseEntity.ok("Subscription created successfully.");
            } else {
                User user = paymentRequest.getUser();
                if (user == null) {
                    throw new IllegalArgumentException("User information is required for placing an order.");
                }

                // Fetch bouquet
                Bouquet bouquet = bouquetService.getBouquetById(paymentRequest.getBouquetId());
                List<Bouquet> bouquets = List.of(bouquet);

                // Place the order
                orderService.createOrder(user, bouquets);
                return ResponseEntity.ok("Order placed successfully.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during checkout: " + e.getMessage());
        }
    }
}
