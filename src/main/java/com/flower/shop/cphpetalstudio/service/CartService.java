package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Cart;
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
    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, BouquetRepository bouquetRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
    }

    // Method to add bouquet to cart
    public CartItem addBouquetToCart(String username, Long bouquetId, int quantity) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the bouquet by bouquetId
        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Bouquet not found"));

        // Find if the user already has this bouquet in the cart
        Optional<CartItem> existingItem = cartItemRepository.findByCart_UserAndBouquet(user, bouquet);

        if (existingItem.isPresent()) {
            // If the bouquet is already in the cart, update the quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity); // Add to the existing quantity
            return cartItemRepository.save(item);
        } else {
            // Otherwise, create a new CartItem
            Cart cart = user.getCart(); // Assuming User has a getCart() method
            CartItem cartItem = new CartItem(cart, bouquet, quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    // Method to remove bouquet from cart
    public void removeBouquetFromCart(String username, Long cartItemId) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the cart item by cartItemId
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ensure the user is authorized to remove the cart item
        if (!cartItem.getCart().getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        // Delete the cart item
        cartItemRepository.delete(cartItem);
    }

    // Method to get cart items for a user
    public List<CartItem> getCartForUser(String username) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch and return the cart items for the user
        return cartItemRepository.findByCart_User(user);
    }

    // Method to update the quantity of a cart item
    public CartItem updateCartItem(String username, Long cartItemId, int newQuantity) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the cart item by cartItemId
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ensure the user is authorized to update the cart item
        if (!cartItem.getCart().getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        // Update the quantity of the cart item
        cartItem.setQuantity(newQuantity);
        return cartItemRepository.save(cartItem);
    }

    // Method to clear a user's cart
    public void clearCart(String username) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete all cart items associated with the user
        cartItemRepository.deleteByCart_User(user);
    }

    // Method to get the count of cart items for a user
    public int getCartItemCount(String username) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the count of cart items for the user
        return (int) cartItemRepository.countByCart_User(user);
    }
}
