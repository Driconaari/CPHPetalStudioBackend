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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

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

    // Add a bouquet to the user's cart
    public CartItem addBouquetToCart(String username, Long bouquetId, int quantity) {
        logger.info("Adding bouquet (ID: {}) to the cart for user: {}", bouquetId, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> {
                    logger.error("Bouquet not found: {}", bouquetId);
                    return new RuntimeException("Bouquet not found");
                });

        // Check if the item already exists in the cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);
        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Increment quantity
            logger.info("Updated quantity of bouquet (ID: {}) in cart for user: {}", bouquetId, username);
        } else {
            cartItem = new CartItem(user, bouquet, quantity);
            logger.info("Added new bouquet (ID: {}) to cart for user: {}", bouquetId, username);
        }

        return cartItemRepository.save(cartItem);
    }

    // Remove a bouquet from the user's cart
    public void removeBouquetFromCart(String username, Long cartItemId) {
        logger.info("Removing bouquet from cart (CartItem ID: {}) for user: {}", cartItemId, username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> {
                    logger.error("Cart item not found: {}", cartItemId);
                    return new RuntimeException("Cart item not found");
                });

        // Ensure the user owns the cart item
        if (!cartItem.getUser().equals(user)) {
            logger.error("Unauthorized access to cart item: CartItem ID: {}, User: {}", cartItemId, username);
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
        logger.info("Removed bouquet (ID: {}) from cart for user: {}", cartItem.getBouquet().getId(), username);
    }

    // Retrieve the cart for the logged-in user
    public List<CartItem> getCartForUser(String username) {
        logger.info("Fetching cart for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        logger.info("Retrieved {} cart items for user: {}", cartItems.size(), username);
        return cartItems;
    }

    // Update the quantity of a cart item
    public CartItem updateCartItem(String username, Long cartItemId, int newQuantity) {
        logger.info("Updating cart item (ID: {}) for user: {} with new quantity: {}", cartItemId, username, newQuantity);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> {
                    logger.error("Cart item not found: {}", cartItemId);
                    return new RuntimeException("Cart item not found");
                });

        // Ensure the user owns the cart item
        if (!cartItem.getUser().equals(user)) {
            logger.error("Unauthorized access to cart item: CartItem ID: {}, User: {}", cartItemId, username);
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    // Clear the cart for the user
    public void clearCart(String username) {
        logger.info("Clearing cart for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        cartItemRepository.deleteByUser(user);
        logger.info("Cleared all cart items for user: {}", username);
    }
}
