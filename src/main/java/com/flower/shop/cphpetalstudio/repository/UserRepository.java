package com.flower.shop.cphpetalstudio.repository;


import com.flower.shop.cphpetalstudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}