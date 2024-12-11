package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import com.flower.shop.cphpetalstudio.entity.*;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final BouquetService bouquetService;

    @Autowired
    public CartController(UserService userService, CartService cartService, CartItemRepository cartItemRepository, BouquetService bouquetService) {
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.bouquetService = bouquetService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        logger.info("Add to cart request received: {}", request);

        User user = userService.findByUsername(authentication.getName());
        Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());

        if (bouquet == null) {
            logger.error("Bouquet not found for ID: {}", request.getBouquetId());
            return ResponseEntity.badRequest().body(null);
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user);
            user.setCart(cart);
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);
        CartItem item;
        if (existingItem.isPresent()) {
            item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = new CartItem();
            item.setUser(user);
            item.setBouquet(bouquet);
            item.setQuantity(request.getQuantity());
            item.setCart(cart);
        }
        cartItemRepository.save(item);
        logger.info("Item added to cart: {}", item);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        logger.info("Removing bouquet from cart - bouquetId: {}", request.getBouquetId());

        try {
            User user = userService.findByUsername(authentication.getName());
            cartService.removeFromCart(user, request.getBouquetId());
            return ResponseEntity.ok("Item removed from cart");
        } catch (Exception e) {
            logger.error("Error removing from cart: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove from cart");
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestParam Long userId) {
        User user = userService.findById(userId);
        List<CartItem> cartItems = cartService.getCartByUser(user);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        User user = userService.findById(userId);
        cartService.clearCart(user);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        int count = cartItemRepository.countByUser(user);
        return ResponseEntity.ok(count);
    }
}