package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import com.flower.shop.cphpetalstudio.repository.BouquetRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BouquetRepository bouquetRepository;
    private Map<Long, CartItem> cart = new HashMap<>();

    @Autowired
    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       BouquetRepository bouquetRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
    }

    /*
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


     */
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

    public CartItem removeFromCart(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cartItem.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        cartItemRepository.delete(cartItem);
        return cartItem;
    }




    public void addToCart(CartItem cartItem) {
        Long itemId = cartItem.getBouquetId();
        if (cart.containsKey(itemId)) {
            CartItem existingItem = cart.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());  // Update quantity if item already in cart
        } else {
            cart.put(itemId, cartItem);  // Add new item if not present in cart
        }
    }

    public void removeFromCart(Long itemId) {
        cart.remove(itemId);  // Remove item from the cart by its item ID
    }

    public int getCartCount() {
        // Return total count of items in the cart. Assuming count is quantity of all items.
        return cart.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public CartItem addBouquetToCart(String username, @NonNull Long bouquetId, @Min(1) int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Bouquet not found"));

        CartItem cartItem = new CartItem(user, bouquet, quantity);
        return cartItemRepository.save(cartItem);

    }
}
