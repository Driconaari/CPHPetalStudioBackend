package com.flower.shop.cphpetalstudio.service;


import com.flower.shop.cphpetalstudio.entity.Subscription;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

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

    public Subscription createSubscription(Subscription subscription) {
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