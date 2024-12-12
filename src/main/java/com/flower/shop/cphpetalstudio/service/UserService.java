package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import com.flower.shop.cphpetalstudio.repository.CartRepository; // Assuming you have a CartRepository
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository; // Assuming CartRepository exists
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // Set default role
        // Make sure to persist the cart before the user if it's not already saved
        if (user.getCart() != null && user.getCart().getId() == null) {
            cartRepository.save(user.getCart()); // Save the Cart first if it's not already saved
        }
        return userRepository.save(user);
    }

    public boolean isUserAdmin(String username) {
        User user = getUserByUsername(username);
        return "ROLE_ADMIN".equals(user.getRole());
    }

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + name));
    }

    public User saveUser(User user) {
        // Save the Cart first if it's not already saved
        if (user.getCart() != null && user.getCart().getId() == null) {
            cartRepository.save(user.getCart()); // Save the Cart before saving the User
        }
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public User getCurrentUser(HttpServletRequest request) {
        // Get the logged-in user from the request
        String username = request.getUserPrincipal().getName();
        return getUserByUsername(username);
    }

    // Other methods...

}
