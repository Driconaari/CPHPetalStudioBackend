package com.flower.shop.cphpetalstudio.repository;

import com.flower.shop.cphpetalstudio.entity.Cart;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find CartItems by User through Cart
    List<CartItem> findByCart_User(User user);

    // Find CartItem by Cart and Bouquet (Cart -> User -> Bouquet)
    Optional<CartItem> findByCart_UserAndBouquet(User user, Bouquet bouquet);

    // Delete CartItems by User through Cart
    void deleteByCart_User(User user);

    // Count CartItems for a User through Cart
    long countByCart_User(User user);

    // Check existence of CartItem by User and Bouquet through Cart
    boolean existsByCart_UserAndBouquet(User user, Bouquet bouquet);

    // Find CartItems for a User ordered by creation date (through Cart)
    List<CartItem> findByCart_UserOrderByCreatedAtDesc(User user);

    // Find CartItem by User and Bouquet_Id through Cart (Cart -> User -> Bouquet -> Bouquet_Id)
    Optional<CartItem> findByCart_UserAndBouquet_Id(User user, Long bouquetId);

    List<CartItem> findByCart(Cart cart);

    void deleteAllByCart(Cart cart);

    int countByCart(Cart cart);
}