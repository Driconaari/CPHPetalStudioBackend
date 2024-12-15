package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.PaymentRequest;
import com.flower.shop.cphpetalstudio.entity.Subscription;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.SubscriptionService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Subscription>> getActiveSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getActiveSubscriptions());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Subscription>> getUserSubscriptions(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(user));
    }

    @PostMapping
    public ResponseEntity<?> createSubscription(@RequestBody Subscription subscription, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            // Convert Subscription to PaymentRequest
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setBouquetId(subscription.getBouquet().getId());
            paymentRequest.setQuantity(1); // Assuming quantity is 1 for subscriptions
            paymentRequest.setSubscription(true);
            paymentRequest.setPaymentPlan(subscription.getFrequency());
            paymentRequest.setUser(user);

            Subscription createdSubscription = subscriptionService.createSubscription(paymentRequest);
            return ResponseEntity.ok(createdSubscription);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while creating the subscription: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscriptionDetails) {
        try {
            Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
            return ResponseEntity.ok(updatedSubscription);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long id) {
        try {
            subscriptionService.deleteSubscription(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
