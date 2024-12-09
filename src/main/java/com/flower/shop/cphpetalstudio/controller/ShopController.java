package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.ResponseMessage;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final BouquetService bouquetService;
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService; // Assuming you have a CartService.

    @Autowired
    public ShopController(BouquetService bouquetService, OrderService orderService,
                          UserService userService, CartService cartService) {
        this.bouquetService = bouquetService;
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    // Fetch all bouquets with optional filters
    @GetMapping
    public List<Bouquet> getAllBouquets(
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) String category) {
        if (maxPrice != null) return bouquetService.getBouquetsUnderPrice(maxPrice);
        if (minPrice != null) return bouquetService.getBouquetsOverPrice(minPrice);
        if (category != null) return bouquetService.getBouquetsByCategory(category);
        return bouquetService.getAllBouquets();
    }

    // Get a specific bouquet by ID
    @GetMapping("/{id}")
    public Bouquet getBouquetById(@PathVariable Long id) {
        return bouquetService.getBouquetById(id);
    }

    // Add a bouquet to the user's cart
    @PostMapping("/cart/add")
    public CartItem addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());
        return cartService.addToCart(user, bouquet, request.getQuantity());
    }

    // View the user's cart
    @GetMapping("/cart")
    public List<CartItem> viewCart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return cartService.getCartByUser(user); // Returns a list of cart items
    }


    // Remove an item from the cart
    @PostMapping("/cart/remove")
    public CartItem removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return cartService.removeFromCart(user, request.getBouquetId());
    }

    // Place an order from the cart
    @PostMapping("/order")
    public Order createOrder(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return orderService.createOrderFromCart(user);
    }

    // View a specific order by ID
    @GetMapping("/order/{id}")
    public Order viewOrder(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Order order = orderService.getOrderById(id);
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to view this order.");
        }
        return order;
    }


    // Example in Spring Boot (Java)
    @PostMapping("/cart/add/{bouquetId}")
    public ResponseEntity<?> addToCart(@PathVariable Long bouquetId, @RequestBody CartItem cartItem) {
        cartService.addToCart(cartItem);
        return ResponseEntity.ok(new ResponseMessage("Item added to cart", true));
    }

    @PostMapping("/cart/remove/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return ResponseEntity.ok(new ResponseMessage("Item removed from cart", true));
    }

    @GetMapping("/cart/count")
    public ResponseEntity<Integer> getCartCount() {
        int count = cartService.getCartCount();
        return ResponseEntity.ok(count);
    }

}
