package com.flower.shop.cphpetalstudio.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Bouquet bouquet;

    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency; // e.g., "WEEKLY", "MONTHLY"
}
