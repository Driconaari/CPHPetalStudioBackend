package com.flower.shop.cphpetalstudio.controller;

import com.flower.shop.cphpetalstudio.dto.*;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;  // Service for User
    private final BouquetService bouquetService;  // Service for Bouquet

    @Autowired
    public CartController(CartService cartService, UserService userService, BouquetService bouquetService) {
        this.cartService = cartService;
        this.userService = userService;
        this.bouquetService = bouquetService;
    }


    // View all items in the cart
    @GetMapping
    public List<CartItem> viewCart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return cartService.getCartByUser(user);
    }

    // Remove an item from the cart
    @PostMapping("/remove")
    public void removeFromCart(@RequestBody RemoveFromCartRequest request, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        cartService.removeFromCart(user, request.getBouquetId());
    }
}
