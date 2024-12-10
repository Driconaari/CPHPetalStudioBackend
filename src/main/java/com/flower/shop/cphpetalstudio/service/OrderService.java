package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.DTO.PaymentRequest;
import com.flower.shop.cphpetalstudio.entity.Bouquet;
import com.flower.shop.cphpetalstudio.entity.Order;
import com.flower.shop.cphpetalstudio.entity.User;

import com.flower.shop.cphpetalstudio.repository.OrderRepository;
import com.flower.shop.cphpetalstudio.exception.InsufficientStockException;
import com.flower.shop.cphpetalstudio.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final BouquetService bouquetService;

    @Autowired
    public OrderService(OrderRepository orderRepository, BouquetService bouquetService) {
        this.orderRepository = orderRepository;
        this.bouquetService = bouquetService;
    }

    public List<Order> getRecentOrdersForUser(User user) {
        return orderRepository.findTop5ByUserOrderByOrderDateDesc(user);
    }

    public Order createOrder(User user, List<Bouquet> bouquets) {
        Order order = new Order();
        order.setUser(user);
        order.setBouquets(bouquets);
        order.setTotal(calculateTotal(bouquets));
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        updateBouquetStock(bouquets);

        return orderRepository.save(order);
    }

    public void createOrder(PaymentRequest paymentRequest) {
        // Fetch bouquet details using the bouquet service
        Bouquet bouquet = bouquetService.getBouquetById(paymentRequest.getBouquetId());

        // Check stock availability
        if (bouquet.getStockQuantity() < paymentRequest.getQuantity()) {
            throw new InsufficientStockException("Not enough stock for bouquet: " + bouquet.getName());
        }

        // Create a new order
        Order order = new Order();
        order.setBouquets(List.of(bouquet));
        order.setTotal(calculateTotal(List.of(bouquet)) * paymentRequest.getQuantity());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        // Deduct the stock
        bouquetService.updateBouquetStock(bouquet.getId(), -paymentRequest.getQuantity());

        // Save the order
        orderRepository.save(order);
    }

    private double calculateTotal(List<Bouquet> bouquets) {
        return bouquets.stream().mapToDouble(Bouquet::getPrice).sum();
    }

    private void updateBouquetStock(List<Bouquet> bouquets) {
        for (Bouquet bouquet : bouquets) {
            if (bouquet.getStockQuantity() > 0) {
                bouquetService.updateBouquetStock(bouquet.getId(), -1);
            } else {
                throw new InsufficientStockException("Insufficient stock for bouquet: " + bouquet.getName());
            }
        }
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

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

    private void restoreBouquetStock(List<Bouquet> bouquets) {
        for (Bouquet bouquet : bouquets) {
            bouquetService.updateBouquetStock(bouquet.getId(), 1);
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersForUserBetweenDates(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByUserAndOrderDateBetween(user, startDate, endDate);
    }
}
