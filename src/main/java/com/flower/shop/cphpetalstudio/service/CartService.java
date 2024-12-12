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
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final UserService userService;
    private final BouquetService bouquetService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository, UserRepository userRepository, UserService userService, BouquetService bouquetService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bouquetService = bouquetService;
    }

    @Transactional
    public void addToCart(User user, Bouquet bouquet, int quantity) {
        // Find the user's cart or create a new one if it doesn't exist
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            // Create a new Cart if the user doesn't have one
            Cart newCart = new Cart(user); // Associate the cart with the user
            return cartRepository.save(newCart); // Save the new cart
        });

        // Now that we have the cart, we can add the bouquet to it.
        // You might need a CartItem entity here if you're associating multiple items with a cart.
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBouquet(bouquet);
        cartItem.setQuantity(quantity);

        // Save the cartItem in the repository
        cartItemRepository.save(cartItem);
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

        // Calculate the total price of all items in the cart
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
                .mapToDouble(cartItem -> {
                    BigDecimal price = cartItem.getBouquet().getPrice(); // Price as BigDecimal
                    BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity()); // Convert quantity to BigDecimal
                    return price.multiply(quantity).doubleValue(); // Multiply BigDecimal values and convert result to double
                })
                .sum();
    }

    public int getCartCount(User user) {
        // Count the number of items in the cart
        return cartItemRepository.countByUser(user);
    }
}
