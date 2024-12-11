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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    private final BouquetService bouquetService;
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public ShopController(BouquetService bouquetService, OrderService orderService,
                          UserService userService, CartService cartService) {
        this.bouquetService = bouquetService;
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
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
    public Bouquet getBouquetById(@PathVariable Long id) {
        logger.info("Fetching bouquet by ID: {}", id);
        return bouquetService.getBouquetById(id);
    }

    @PostMapping("/cart/add")
    public CartItem addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        logger.info("Adding bouquet to cart - bouquetId: {}, quantity: {}", request.getBouquetId(), request.getQuantity());
        User user = userService.findByUsername(authentication.getName());
        Bouquet bouquet = bouquetService.getBouquetById(request.getBouquetId());
        return cartService.addToCart(user, bouquet, request.getQuantity());
    }

    @GetMapping("/cart")
    public List<CartItem> viewCart(Authentication authentication) {
        logger.info("Viewing cart for user: {}", authentication.getName());
        User user = userService.findByUsername(authentication.getName());
        return cartService.getCartByUser(user);
    }

    @PostMapping("/cart/remove")
    public CartItem removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        logger.info("Removing bouquet from cart - bouquetId: {}", request.getBouquetId());
        User user = userService.findByUsername(authentication.getName());
        return cartService.removeFromCart(user, request.getBouquetId());
    }

    @PostMapping("/order")
    public Order createOrder(Authentication authentication) {
        logger.info("Creating order for user: {}", authentication.getName());
        User user = userService.findByUsername(authentication.getName());
        return orderService.createOrderFromCart(user);
    }

    @GetMapping("/order/{id}")
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

    @PostMapping("/cart/add/{bouquetId}")
    public ResponseEntity<?> addToCart(@PathVariable Long bouquetId, @RequestBody CartItem cartItem) {
        logger.info("Adding bouquet to cart - bouquetId: {}", bouquetId);
        cartService.addToCart(cartItem);
        return ResponseEntity.ok(new ResponseMessage("Item added to cart", true));
    }

    @PostMapping("/cart/remove/{itemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        logger.info("Removing item from cart - itemId: {}", itemId);
        cartService.removeFromCart(itemId);
        return ResponseEntity.ok(new ResponseMessage("Item removed from cart", true));
    }

    @GetMapping("/cart/count")
    public ResponseEntity<Integer> getCartCount() {
        logger.info("Fetching cart count");
        int count = cartService.getCartCount();
        return ResponseEntity.ok(count);
    }
}