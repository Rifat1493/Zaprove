package com.jamiur.core;

import com.jamiur.core.controller.UserController;
import com.jamiur.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class RbacAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void helloWorld_returnsOk() throws Exception {
        mockMvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World! UserController is working!"));
    }

    // If you want to test security, use @SpringBootTest with full context
    // @Test
    // @WithMockUser(username = "admin", roles = {"ADMIN"})
    // void helloWorld_withAdminRole_returnsOk() throws Exception {
    //     mockMvc.perform(get("/api/users/hello"))
    //             .andExpect(status().isOk())
    //             .andExpect(content().string("Hello World! UserController is working!"));
    // }
}