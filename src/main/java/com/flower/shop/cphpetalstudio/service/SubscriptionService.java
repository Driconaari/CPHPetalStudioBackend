package com.flower.shop.cphpetalstudio.service;


import com.flower.shop.cphpetalstudio.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private com.flower.shop.cphpetalstudio.repository.SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAllActiveSubscriptions() {
        return subscriptionRepository.findByEndDateAfter(LocalDate.now());
    }

    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Subscription not found"));
    }
}