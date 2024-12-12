package com.flower.shop.cphpetalstudio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Corrected mapping to match the user side
    @OneToOne(mappedBy = "cart")  // "cart" in the User class is the owning side
    private User user;

    private LocalDateTime createdAt;

    public Cart(User user) {
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
