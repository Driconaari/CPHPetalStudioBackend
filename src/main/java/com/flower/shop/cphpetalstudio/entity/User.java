package com.flower.shop.cphpetalstudio.entity;


import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role; // "ROLE_USER" or "ROLE_ADMIN"
    private String email;
    private boolean isCompany;
}