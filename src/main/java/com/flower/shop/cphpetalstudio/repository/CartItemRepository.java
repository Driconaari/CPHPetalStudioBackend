package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndBouquet(User user, Bouquet bouquet);

    void deleteByUser(User user);

    long countByUser(User user);

    boolean existsByUserAndBouquet(User user, Bouquet bouquet);

    List<CartItem> findByUserOrderByCreatedAtDesc(User user);

    // Corrected method to reference bouquet's id using '_Id'
    Optional<CartItem> findByUserAndBouquet_Id(User user, Long bouquetId);


}
