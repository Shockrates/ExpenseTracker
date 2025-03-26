package com.sokratis.ExpenseTracker.Controller;

import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Service.JWTService;
import com.sokratis.ExpenseTracker.Service.UserService;
import com.sokratis.ExpenseTracker.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Simulates HTTP requests

    @MockBean
    private UserService userService;  // Mocked service

    @MockBean
    private SecurityUtils securityUtils; // Mocking SecurityUtils


    @MockBean
    private JWTService jwtTokenProvider; // Mocked JWT Provider

    @Autowired
    private ObjectMapper objectMapper; // Converts JSON

    private User validUserRequest;
    private User invalidUserRequest;

    @BeforeEach
    void setUp() {
        validUserRequest = new User((long) 1, "John Doe", "john.doe@example.com", "password123", "");
        invalidUserRequest = new User((long) 1, "John Doe", "", "password123",""); // Missing email
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Simulates an Admin user
    void testGetAllUsers_AsAdmin_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"}) // Normal user
    void testGetAllUsers_AsUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden()); // Expect HTTP 403 Forbidden
    }

    @Test
    void testGetAllUsers_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized()); // Expect HTTP 401 Unauthorized
    }

    @Test
    void testRegisterUser_Success() throws Exception {

        
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isCreated());
       
    }

    @Test
    void testRegisterUser_FailsWithoutEmail() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserRequest)))
                .andExpect(status().isBadRequest()); // Expect 400 Bad Request
    }
}

