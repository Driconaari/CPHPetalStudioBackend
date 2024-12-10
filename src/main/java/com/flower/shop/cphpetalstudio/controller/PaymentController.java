package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.DTO.PaymentRequest;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody PaymentRequest paymentRequest) {
        if (paymentRequest.isSubscription()) {
            subscriptionService.createSubscription(paymentRequest);
            return ResponseEntity.ok("Subscription created successfully.");
        } else {
            orderService.createOrder(paymentRequest);
            return ResponseEntity.ok("Order placed successfully.");
        }
    }
}
