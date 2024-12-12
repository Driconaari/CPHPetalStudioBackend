package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Cart;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository; // Assuming you have a CartRepository for Cart persistence
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public CartItem addToCart(User user, Bouquet bouquet, int quantity) {
        logger.info("Adding bouquet to cart: {}", bouquet.getName());

        // Retrieve or create the cart for the user
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user);
            cart.setCreatedAt(LocalDateTime.now()); // Set the created_at field
            user.setCart(cart);
        }

        // Find existing cart item
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setUser(user);
            newCartItem.setBouquet(bouquet);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);
            newCartItem.setCreatedAt(LocalDateTime.now()); // Set created_at for CartItem
            return cartItemRepository.save(newCartItem);
        }
    }


    public List<CartItem> getCartByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void removeFromCart(User user, Long bouquetId) {
        CartItem cartItem = cartItemRepository.findByUserAndBouquet_Id(user, bouquetId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }

    public double getTotalPrice(User user) {
        // Calculate total price of all items in the cart
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getBouquet().getPrice() * cartItem.getQuantity())
                .sum();
    }

}
