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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bouquet_id", nullable = false)
    private Bouquet bouquet;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public CartItem(User user, Bouquet bouquet, int quantity, Cart cart) {
        this.user = user;
        this.bouquet = bouquet;
        this.quantity = quantity;
        this.cart = cart;
    }

    public Long getBouquetId() {
        return bouquet != null ? bouquet.getId() : null;
    }
}
