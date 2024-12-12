package com.flower.shop.cphpetalstudio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensure user is properly set
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bouquet relationship
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bouquet_id", nullable = false)
    private Bouquet bouquet;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Cart relationship
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructor with all required fields
    public CartItem(User user, Bouquet bouquet, int quantity, Cart cart) {
        this.user = user;
        this.bouquet = bouquet;
        this.quantity = quantity;
        this.cart = cart;
    }

    // Method to set bouquet ID if bouquet is not provided (initialize if null)
    public void setBouquetId(Long bouquetId) {
        if (bouquet == null) {
            bouquet = new Bouquet();  // Initialize the bouquet if it is null
        }
        bouquet.setId(bouquetId);  // Set the bouquet ID
    }
}
