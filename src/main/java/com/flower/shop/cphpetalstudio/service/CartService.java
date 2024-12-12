package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Cart;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.CartRepository;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository; // Added to fetch users
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private UserService userService;
    private BouquetService bouquetService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository, UserRepository userRepository, UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public void addToCart(User user, Bouquet bouquet, int quantity) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndBouquet(user, bouquet);

        if (existingCartItem.isPresent()) {
            // Update the quantity if the item already exists in the cart
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // Add a new cart item if it's not already in the cart
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBouquet(bouquet);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }



    public List<CartItem> getCartByUser(Long userId) {
        User user = userService.findById(userId);
        return cartItemRepository.findByUser(user);
    }


    public void removeFromCart(User user, Long bouquetId) {
        CartItem cartItem = cartItemRepository.findByUserAndBouquet_Id(user, bouquetId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }


    public void clearCart(Long userId) {
        User user = userService.findById(userId);
        cartItemRepository.deleteByUser(user);
    }


    public double getTotalPrice(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate total price of all items in the cart
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
                .mapToDouble(cartItem -> {
                    BigDecimal price = cartItem.getBouquet().getPrice(); // Price as BigDecimal
                    BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity()); // Convert quantity to BigDecimal
                    return price.multiply(quantity).doubleValue(); // Multiply BigDecimal values and convert result to double
                })
                .sum();
    }
}
