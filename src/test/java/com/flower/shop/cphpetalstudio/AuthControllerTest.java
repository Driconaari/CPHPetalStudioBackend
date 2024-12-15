package com.flower.shop.cphpetalstudio;

import com.flower.shop.cphpetalstudio.controller.AuthController;
import com.flower.shop.cphpetalstudio.entity.User;
import com.flower.shop.cphpetalstudio.security.JwtUtil;
import com.flower.shop.cphpetalstudio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// test disabled cause of the error in the test class, cause im not connected to the database and the test class is trying to connect to the database
/*
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserService userService;

    private User user;
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setRole("USER");

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    public void testCreateAuthenticationToken() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(jwtUtil.generateToken(Mockito.any(UserDetails.class)))
                .thenReturn("jwt-token");
        Mockito.when(userService.findByUsername("testuser"))
                .thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"token\":\"jwt-token\",\"username\":\"testuser\",\"role\":\"USER\"}"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setRole("USER");

        Mockito.when(userService.registerUser(Mockito.any(User.class)))
                .thenReturn(newUser);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newuser\",\"password\":\"password\",\"role\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"username\":\"newuser\",\"password\":\"password\",\"role\":\"USER\"}"));
    }
}

 */