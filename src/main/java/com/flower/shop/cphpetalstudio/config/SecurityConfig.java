package com.flower.shop.cphpetalstudio.config;

import com.flower.shop.cphpetalstudio.LoggingFilter;
import com.flower.shop.cphpetalstudio.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure CORS to allow cross-origin requests from the frontend
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:5500")); // Allow frontend to make requests
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*")); // Allow all headers
                    corsConfiguration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)
                    return corsConfiguration;
                }))
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for APIs since we're using JWTs
                .authorizeHttpRequests(authz -> authz
                        // Public Endpoints: Accessible to all users (no authentication required)
                        .requestMatchers("/api/auth/**", "/", "/register", "/login").permitAll()
                        .requestMatchers("/bouquets", "/bouquets/{id}", "/api/bouquets").permitAll()
                        .requestMatchers("/shop/add").permitAll() // Allow anyone to add items to cart
                        .requestMatchers("/add-to-cart").permitAll() // Allow anyone to add items to cart

                        // Admin-Only Endpoints: Accessible only by users with the role "ROLE_ADMIN"
                        .requestMatchers("/bouquets/create", "/bouquets/{id}/edit", "/bouquets/{id}/delete").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/admin/**", "/admin/**").hasAuthority("ROLE_ADMIN")

                        // Authenticated-Only Endpoints: Require authentication for these endpoints
                        .requestMatchers("/dashboard", "/add-to-cart", "/shop/**").authenticated()

                        // Catch-All for All Other Requests: Requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless session (JWT-based authentication)
                )
                .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class) // Custom Logging Filter
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Custom JWT filter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    // Provide AuthenticationManager for authentication-related tasks
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Provide PasswordEncoder for encoding user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt is a strong password hashing algorithm
    }
}
