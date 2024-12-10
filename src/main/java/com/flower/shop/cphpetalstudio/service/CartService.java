package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import com.flower.shop.cphpetalstudio.repository.BouquetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       BouquetRepository bouquetRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
    }

    public CartItem addBouquetToCart(String username, Long bouquetId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Bouquet not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem cartItem = new CartItem(user, bouquet, quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    public void removeBouquetFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getCartForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartItemRepository.findByUser(user);
    }

    public CartItem updateCartItem(String username, Long cartItemId, int newQuantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItemRepository.deleteByUser(user);
    }

    public int getCartItemCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long count = cartItemRepository.countByUser(user);

        // Check if the count exceeds the maximum value of int
        if (count > Integer.MAX_VALUE) {
            throw new ArithmeticException("Cart item count exceeds maximum integer value");
        }

        return (int) count;
    }
}