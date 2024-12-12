package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.CartItem;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.CartItemRepository;
import com.flower.shop.cphpetalstudio.repository.OrderRepository;
import com.flower.shop.cphpetalstudio.exception.InsufficientStockException;
import com.flower.shop.cphpetalstudio.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final BouquetService bouquetService;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BouquetService bouquetService, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.bouquetService = bouquetService;
        this.cartItemRepository = cartItemRepository;
    }

    // Fetch the 5 most recent orders for a user
    public List<Order> getRecentOrdersForUser(User user) {
        return orderRepository.findTop5ByUserOrderByOrderDateDesc(user);
    }

    // Create a new order
    public Order createOrder(User user, List<Bouquet> bouquets) {
        validateStock(bouquets); // Ensure sufficient stock before creating an order

        Order order = new Order();
        order.setUser(user);
        order.setBouquets(bouquets);
        order.setTotal(calculateTotal(bouquets));
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        updateBouquetStock(bouquets);

        return orderRepository.save(order);
    }

// Calculate the total price of the order
    private double calculateTotal(List<Bouquet> bouquets) {
        return bouquets.stream()
                .mapToDouble(bouquet -> bouquet.getPrice().doubleValue())  // Convert BigDecimal to double
                .sum();
    }


    // Update the stock for all bouquets in an order
    private void updateBouquetStock(List<Bouquet> bouquets) {
        for (Bouquet bouquet : bouquets) {
            bouquetService.updateBouquetStock(bouquet.getId(), -1);
        }
    }

    // Ensure all bouquets in the order have sufficient stock
    private void validateStock(List<Bouquet> bouquets) {
        for (Bouquet bouquet : bouquets) {
            if (bouquet.getStockQuantity() <= 0) {
                throw new InsufficientStockException("Insufficient stock for bouquet: " + bouquet.getName());
            }
        }
    }

    // Retrieve an order by its ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    // Fetch all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Update the status of an order
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Cancel an order and restore the stock of its bouquets
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        if ("PLACED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            restoreBouquetStock(order.getBouquets());
        } else {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getStatus());
        }
    }

    // Restore the stock of bouquets in a cancelled order
    private void restoreBouquetStock(List<Bouquet> bouquets) {
        for (Bouquet bouquet : bouquets) {
            bouquetService.updateBouquetStock(bouquet.getId(), 1);
        }
    }

    // Retrieve orders by their status
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // Retrieve orders for a user within a specific date range
    public List<Order> getOrdersForUserBetweenDates(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByUserAndOrderDateBetween(user, startDate, endDate);
    }


    public Order createOrderFromCart(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        List<Bouquet> bouquets = cartItems.stream()
                .map(CartItem::getBouquet)
                .toList();

        Order order = new Order();
        order.setUser(user);
        order.setBouquets(bouquets);

        // Calculate total using BigDecimal for precision
        order.setTotal(cartItems.stream()
                .map(cartItem -> cartItem.getBouquet().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue());  // Convert to double after sum

        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        cartItemRepository.deleteByUser(user); // Clear the cart after placing the order

        return orderRepository.save(order);
    }


}
