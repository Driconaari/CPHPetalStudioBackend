package com.flower.shop.cphpetalstudio.repository;


import com.flower.shop.cphpetalstudio.entity.Subscription;
import com.flower.shop.cphpetalstudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUser(User user);
    List<Subscription> findByEndDateAfter(LocalDate date);
    List<Subscription> findByUserAndEndDateAfter(User user, LocalDate date);
}