package com.flower.shop.cphpetalstudio.config.TestControllers;


import com.flower.shop.cphpetalstudio.controller.CartController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static java.nio.file.Paths.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class) // Test only the CartController
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldAllowAccessToProfileWhenAuthenticated() throws Exception {
        mockMvc.perform((RequestBuilder) get("/api/user/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDenyAccessToProfileWhenNotAuthenticated() throws Exception {
        mockMvc.perform((RequestBuilder) get("/api/user/profile"))
                .andExpect(status().isUnauthorized());
    }
}
