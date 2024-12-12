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

    // Ensure the User class has the correct association with Cart
    @OneToOne
    @JoinColumn(name = "cart_id") // The 'cart_id' will be the foreign key in the User table
    private Cart cart;

    @Column(nullable = false)
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // Default value

    @Column(name = "is_company", nullable = false)
    private boolean isCompany = false; // Default value

    public Set<String> getRoles() {
        return Arrays.stream(role.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
