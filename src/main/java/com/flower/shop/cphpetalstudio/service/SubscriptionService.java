package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.dto.PaymentRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Subscription;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final BouquetService bouquetService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, BouquetService bouquetService) {
        this.subscriptionRepository = subscriptionRepository;
        this.bouquetService = bouquetService;
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }

    public List<Subscription> getSubscriptionsByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }

    public List<Subscription> getActiveSubscriptions() {
        return subscriptionRepository.findByEndDateAfter(LocalDate.now());
    }

    public List<Subscription> getActiveSubscriptionsByUser(User user) {
        return subscriptionRepository.findByUserAndEndDateAfter(user, LocalDate.now());
    }

    // Updated createSubscription method to handle PaymentRequest
    public Subscription createSubscription(PaymentRequest paymentRequest) {
        // Fetch the bouquet
        Bouquet bouquet = bouquetService.getBouquetById(paymentRequest.getBouquetId());

        // Map PaymentRequest to Subscription
        Subscription subscription = new Subscription();
        subscription.setBouquet(bouquet);
        subscription.setUser(paymentRequest.getUser());
        subscription.setFrequency(paymentRequest.getPaymentPlan());
        subscription.setStartDate(LocalDateTime.now());
        subscription.setStatus("ACTIVE");

        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, Subscription subscriptionDetails) {
        Subscription subscription = getSubscriptionById(id);
        subscription.setUser(subscriptionDetails.getUser());
        subscription.setBouquet(subscriptionDetails.getBouquet());
        subscription.setStartDate(subscriptionDetails.getStartDate());
        subscription.setEndDate(subscriptionDetails.getEndDate());
        subscription.setFrequency(subscriptionDetails.getFrequency());
        return subscriptionRepository.save(subscription);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}
