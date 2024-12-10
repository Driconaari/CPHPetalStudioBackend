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

            // Check if the user has a Cart; if not, create one
            Cart cart = user.getCart();  // This assumes the User already has a Cart associated
            if (cart == null) {
                cart = new Cart(); // Create a new Cart if the user doesn't have one
                cart.setUser(user); // Link the Cart to the User
                cart = cartIRepository.save(cart); // Save the newly created Cart to the database
            }

            // Create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);  // Set the newly created or fetched Cart
            cartItem.setBouquet(bouquet);
            cartItem.setQuantity(quantity);

            // Save the CartItem to the repository
            return cartItemRepository.save(cartItem);
        }
    }
}