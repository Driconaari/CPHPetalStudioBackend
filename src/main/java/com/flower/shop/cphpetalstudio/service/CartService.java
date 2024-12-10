package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Cart;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import com.flower.shop.cphpetalstudio.repository.BouquetRepository;
import com.flower.shop.cphpetalstudio.repository.CartRepository;
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
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, BouquetRepository bouquetRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bouquetRepository = bouquetRepository;
        this.cartRepository = cartRepository;
    }

    // Add bouquet to cart
    public CartItem addBouquetToCart(String username, Long bouquetId, int quantity) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Bouquet bouquet = bouquetRepository.findById(bouquetId).orElseThrow(() -> new RuntimeException("Bouquet not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCart_UserAndBouquet(user, bouquet);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity); // Add to existing quantity
            return cartItemRepository.save(item);
        } else {
            Cart cart = user.getCart();
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart);
            }

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBouquet(bouquet);
            cartItem.setQuantity(quantity);

            return cartItemRepository.save(cartItem);
        }
    }

    // Remove bouquet from cart
    public void removeBouquetFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Only delete if the CartItem belongs to the correct User
        if (cartItem.getCart().getUser().equals(user)) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Cart item does not belong to this user.");
        }
    }

    // Get cart for the user
    public List<CartItem> getCartForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        return cartItemRepository.findByCart(cart);
    }

    // Update cart item quantity
    public void updateCartItem(String username, Long cartItemId, int quantity) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));

        if (cartItem.getCart().getUser().equals(user)) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Cart item does not belong to this user.");
        }
    }

    // Clear cart for the user
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        if (cart != null) {
            cartItemRepository.deleteAllByCart(cart);
        }
    }

    // Get cart item count
    public int getCartItemCount(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = user.getCart();
        return cart != null ? cartItemRepository.countByCart(cart) : 0;
    }
}
