package com.flower.shop.cphpetalstudio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Comma-separated roles, e.g., "ROLE_USER,ROLE_ADMIN".
     */
    @Column(nullable = false)
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // Default value

    @Column(name = "is_company", nullable = false)
    private boolean isCompany = false; // Default value

    @OneToOne
    private Cart cart;

    /**
     * Splits the `role` field and returns a set of roles.
     */
    public Set<String> getRoles() {
        return Arrays.stream(role.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }


    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }


    public void setCart(Cart cart) {
    this.cart = cart;
}
}
