package com.flower.shop.cphpetalstudio;// CartControllerTest.java


import com.flower.shop.cphpetalstudio.controller.CartController;
import com.flower.shop.cphpetalstudio.dto.AddToCartRequest;
import com.flower.shop.cphpetalstudio.dto.RemoveFromCartRequest;
import com.flower.shop.cphpetalstudio.entity.*;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.service.BouquetService;
import com.flower.shop.cphpetalstudio.service.CartService;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private BouquetService bouquetService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setBouquetId(1L);
        request.setQuantity(2);

        User user = new User();
        user.setUsername("testUser");

        Bouquet bouquet = new Bouquet();
        bouquet.setId(1L);

        Cart cart = new Cart(user);
        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setBouquet(bouquet);
        cartItem.setQuantity(2);
        cartItem.setCart(cart);

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(bouquetService.getBouquetById(1L)).thenReturn(bouquet);
        when(cartItemRepository.findByUserAndBouquet(user, bouquet)).thenReturn(Optional.of(cartItem));

        ResponseEntity<CartItem> response = cartController.addToCart(request, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveFromCart() {
        RemoveFromCartRequest request = new RemoveFromCartRequest();
        request.setBouquetId(1L);

        User user = new User();
        user.setUsername("testUser");

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        ResponseEntity<?> response = cartController.removeFromCart(request, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(cartService, times(1)).removeFromCart(user, 1L);
    }
}