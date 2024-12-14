package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Cart;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndBouquet(User user, Bouquet bouquet);


    List<CartItem> findByUser(User user);

    void deleteByUser(User user);


    Optional<CartItem> findByUserAndBouquet_Id(User user, Long bouquetId);


    int countByUser(User user);


    List<CartItem> findByCart(Cart cart);




}

