package com.flower.shop.cphpetalstudio.serviceTest;

import com.flower.shop.cphpetalstudio.entity.*;
import com.flower.shop.cphpetalstudio.repository.*;
import com.flower.shop.cphpetalstudio.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BouquetRepository bouquetRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBouquetToCart() {
        String username = "testuser";
        Long bouquetId = 1L;
        int quantity = 2;

        User user = new User();
        user.setUsername(username);

        Bouquet bouquet = new Bouquet();
        bouquet.setId(bouquetId);

        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bouquetRepository.findById(bouquetId)).thenReturn(Optional.of(bouquet));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndBouquet(cart, bouquet)).thenReturn(Optional.empty());

        CartItem cartItem = new CartItem(cart, bouquet, quantity);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addBouquetToCart(username, bouquetId, quantity);

        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveBouquetFromCart() {
        String username = "testuser";
        Long cartItemId = 1L;

        User user = new User();
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setUser(user);

        Bouquet bouquet = new Bouquet();
        bouquet.setId(1L);

        CartItem cartItem = new CartItem(cart, bouquet, 2);
        cartItem.setId(cartItemId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        cartService.removeBouquetFromCart(username, cartItemId);

        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testGetCartForUser() {
        String username = "testuser";

        User user = new User();
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setUser(user);

        Bouquet bouquet = new Bouquet();
        bouquet.setId(1L);

        CartItem cartItem = new CartItem(cart, bouquet, 2);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(cartItem));

        List<CartItem> cartItems = cartService.getCartForUser(username);

        assertNotNull(cartItems);
        assertEquals(1, cartItems.size());
        assertEquals(cartItem, cartItems.get(0));
    }

    @Test
    void testUpdateCartItem() {
        String username = "testuser";
        Long cartItemId = 1L;
        int newQuantity = 5;

        User user = new User();
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setUser(user);

        Bouquet bouquet = new Bouquet();
        bouquet.setId(1L);

        CartItem cartItem = new CartItem(cart, bouquet, 2);
        cartItem.setId(cartItemId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        cartService.updateCartItem(username, cartItemId, newQuantity);

        assertEquals(newQuantity, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testClearCart() {
        String username = "testuser";

        User user = new User();
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.clearCart(username);

        verify(cartItemRepository, times(1)).deleteAllByCart(cart);
    }

    @Test
    void testGetCartItemCount() {
        String username = "testuser";

        User user = new User();
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.countByCart(cart)).thenReturn(3);

        int itemCount = cartService.getCartItemCount(username);

        assertEquals(3, itemCount);
    }


    @Test
    void testUserCartAssociation() {
        // Create and persist a user
        User user = new User();
        user.setName("Test User");
        user = userRepository.save(user);

        // Create and persist a cart associated with the user
        Cart cart = new Cart();
        cart.setUser(user);
        cart = cartRepository.save(cart);

        // Verify the association
        assertNotNull(cart.getId());
        assertEquals(user.getId(), cart.getUser().getId());
    }

}

