package com.flower.shop.cphpetalstudio;

import com.flower.shop.cphpetalstudio.security.JwtRequestFilter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//test disabled cause of the error in the test class, cause im not connected to the database and the test class is trying to connect to the database
//to test the security configuration

/*

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.doNothing().when(jwtRequestFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/auth/login")).andExpect(status().isOk());
        mockMvc.perform(get("/bouquets")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminEndpoints() throws Exception {
        mockMvc.perform(post("/bouquets/create").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedEndpoints() throws Exception {
        mockMvc.perform(get("/dashboard")).andExpect(status().isOk());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/dashboard")).andExpect(status().isUnauthorized());
    }

    @Configuration
    static class TestConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }
}

 */