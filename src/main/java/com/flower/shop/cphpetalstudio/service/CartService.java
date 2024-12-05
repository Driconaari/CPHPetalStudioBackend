package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void addBouquetToCart(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public void removeBouquetFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }

    public List<CartItem> getCartForUser() {
        // Assuming you have a method to get the current user's ID
        Long userId = getCurrentUserId();
        return cartItemRepository.findByUserId(userId);
    }

    private Long getCurrentUserId() {
        // Implement logic to get the current user's ID
        return 1L; // Placeholder
    }
}