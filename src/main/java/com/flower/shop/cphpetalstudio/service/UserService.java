package com.flower.shop.cphpetalstudio.service;

import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public User createAdminUser(String username, String password, String email) {
        User adminUser = new User();
        adminUser.setUsername(username);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setEmail(email);
        adminUser.setRole("ROLE_ADMIN");
        return userRepository.save(adminUser);
    }

    // Add other user-related methods
}