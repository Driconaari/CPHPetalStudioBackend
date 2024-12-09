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

    // Add a bouquet to the user's cart
    public CartItem addBouquetToCart(String username, Long bouquetId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Bouquet not found"));

        // Check if the item already exists in the cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity); // Increment quantity
            return cartItemRepository.save(item);
        } else {
            // Create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBouquet(bouquet);
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    // Remove a bouquet from the user's cart
    public void removeBouquetFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ensure the user owns the cart item
        if (!cartItem.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    // Retrieve the cart for the logged-in user
    public List<CartItem> getCartForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartItemRepository.findByUser(user);
    }

    // Update the quantity of a cart item
    public CartItem updateCartItem(String username, Long cartItemId, int newQuantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ensure the user owns the cart item
        if (!cartItem.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    // Clear the cart for the user
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cartItemRepository.deleteByUser(user);
    }


    public CartItem addToCart(User user, Bouquet bouquet, int quantity) {
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem(user, bouquet, quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    public List<CartItem> getCartByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem removeFromCart(User user, Long bouquetId) {
        CartItem cartItem = cartItemRepository.findByUserAndBouquetId(user, bouquetId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem); // Delete the item
        return cartItem; // Return the deleted item
    }


}
