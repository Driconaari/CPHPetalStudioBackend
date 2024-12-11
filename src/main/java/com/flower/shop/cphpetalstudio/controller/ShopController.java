package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.OrderService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
/*
@Controller
@RequestMapping("/shop")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    private final BouquetService bouquetService;
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public ShopController(BouquetService bouquetService, OrderService orderService, UserService userService, CartService cartService, CartItemRepository cartItemRepository) {
        this.bouquetService = bouquetService;
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
    }

    @GetMapping
    public String getShopPage(@RequestParam(required = false) BigDecimal maxPrice,
                              @RequestParam(required = false) BigDecimal minPrice,
                              @RequestParam(required = false) String category,
                              Model model) {
        logger.info("Fetching shop page with filters - maxPrice: {}, minPrice: {}, category: {}", maxPrice, minPrice, category);
        List<Bouquet> bouquets;

        if (maxPrice != null) {
            bouquets = bouquetService.getBouquetsUnderPrice(maxPrice);
        } else if (minPrice != null) {
            bouquets = bouquetService.getBouquetsOverPrice(minPrice);
        } else if (category != null) {
            bouquets = bouquetService.getBouquetsByCategory(category);
        } else {
            bouquets = bouquetService.getAllBouquets();
        }

        model.addAttribute("bouquets", bouquets);
        return "shop";
    }

    @GetMapping("/bouquets")
    @ResponseBody
    public List<Bouquet> getAllBouquets(@RequestParam(required = false) BigDecimal maxPrice,
                                        @RequestParam(required = false) BigDecimal minPrice,
                                        @RequestParam(required = false) String category) {
        logger.info("Fetching all bouquets with filters - maxPrice: {}, minPrice: {}, category: {}", maxPrice, minPrice, category);
        if (maxPrice != null) return bouquetService.getBouquetsUnderPrice(maxPrice);
        if (minPrice != null) return bouquetService.getBouquetsOverPrice(minPrice);
        if (category != null) return bouquetService.getBouquetsByCategory(category);
        return bouquetService.getAllBouquets();
    }

    @GetMapping("/bouquets/{id}")
    @ResponseBody
    public Bouquet getBouquetById(@PathVariable Long id) {
        logger.info("Fetching bouquet by ID: {}", id);
        return bouquetService.getBouquetById(id);
    }

    @PostMapping("/order")
    @ResponseBody
    public Order createOrder(Authentication authentication) {
        logger.info("Creating order for user: {}", authentication.getName());
        User user = userService.findByUsername(authentication.getName());
        return orderService.createOrderFromCart(user);
    }

    @GetMapping("/order/{id}")
    @ResponseBody
    public Order viewOrder(@PathVariable Long id, Authentication authentication) {
        logger.info("Viewing order - orderId: {}", id);
        User user = userService.findByUsername(authentication.getName());
        Order order = orderService.getOrderById(id);
        if (!order.getUser().equals(user)) {
            logger.warn("Access denied for user: {} to order: {}", user.getUsername(), id);
            throw new AccessDeniedException("You are not authorized to view this order.");
        }
        return order;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());

        if (bouquet == null) {
            return ResponseEntity.badRequest().body(null); // Return a bad request if bouquet is not found
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndBouquet(user, bouquet);
        CartItem item;
        if (existingItem.isPresent()) {
            item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = new CartItem(user, bouquet, request.getQuantity());
        }
        cartItemRepository.save(item);
        return ResponseEntity.ok(item); // Return the added/updated cart item
    }

    @PostMapping("/remove")
    @ResponseBody
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

    @GetMapping("/cart/count")
    public ResponseEntity<Integer> getCartCount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        int count = cartItemRepository.countByUser (user);
        return ResponseEntity.ok(count);
    }
}

 */