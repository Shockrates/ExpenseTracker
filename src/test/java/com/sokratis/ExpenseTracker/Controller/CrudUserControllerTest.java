package com.sokratis.ExpenseTracker.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Repository.UserRepository;
import com.sokratis.ExpenseTracker.Service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class CrudUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    @Mock
    private UserService userService;  // Mock the service layer

    @InjectMocks
    private UserController userController;  // Inject the mock service into the controller

    @Autowired
    private UserRepository userRepository; // Cleanup user data

    private String jwtToken; // Store JWT for authentication
    private String testEmail;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        // 1️⃣ Create a test user
        // Ensure unique email per test
        testEmail = "john" + System.currentTimeMillis() + "@example.com";
        user = new User(null, "John Doe", testEmail, "password123", "");
         // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        // 2️⃣ Login to get JWT token
        jwtToken = authenticateAndGetToken(testEmail, "password123");
    }

    // @AfterEach
    // void cleanup() {
    //     userRepository.deleteAll(); // Clean up users after each test
    // }

    // ✅ Helper method to authenticate and return JWT token
    private String authenticateAndGetToken(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userEmail\":\"" + email + "\", \"userPassword\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // Extract JWT token from "data" field
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("data").asText();
    }

    // ✅ Test: User Registration (No Auth Required)
    @Test
    void testCreateUser() throws Exception {
    // Create a JSON request for a new user
    String userJson = """
            {
                "userName": "Tester ADMIN 2",
                "userEmail": "TesterAdmin2@example.com",
                "userPassword": "password123"
            }
            """;

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated()) // 201 Created
            .andExpect(jsonPath("$.message").value("User Created")) // Validate message
            .andExpect(jsonPath("$.data.userId").isNumber()) // Check if userId exists
            .andExpect(jsonPath("$.data.userName").value(user.getUserName())) // Validate userName
            .andExpect(jsonPath("$.data.userEmail").value(user.getUserEmail())); // Validate email
}
    // ✅ Test: Get User (Requires Auth)
    // @Test
    // void testGetUser() throws Exception {
    //     mockMvc.perform(get("/api/users/me")
    //             .header("Authorization", "Bearer " + jwtToken))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.email").value("john@example.com"));
    // }

    // ✅ Test: Update User (Requires Auth)
    // @Test
    // void testUpdateUser() throws Exception {
    //     UserDTO updatedUser = new UserDTO(null, "John Updated", "john@example.com");

    //     mockMvc.perform(put("/api/users/me")
    //             .header("Authorization", "Bearer " + jwtToken)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(updatedUser)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.name").value("John Updated"));
    // }

    // ✅ Test: Delete User (Requires Auth)
    // @Test
    // void testDeleteUser() throws Exception {
    //     mockMvc.perform(delete("/api/users/me")
    //             .header("Authorization", "Bearer " + jwtToken))
    //             .andExpect(status().isNoContent());
    // }

    // ✅ Test: Unauthorized Access to Protected Routes
    // @Test
    // void testUnauthorizedAccess() throws Exception {
    //     mockMvc.perform(get("/api/users/me"))
    //             .andExpect(status().isUnauthorized()); // No token should return 401
    // }

    // ✅ Test: Login and Get JWT Token
    // @Test
    // void testLogin() throws Exception {
    //     MvcResult result = mockMvc.perform(post("/api/auth/login")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content("{\"email\":\"john@example.com\", \"password\":\"password123\"}"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.token").exists()) // Ensure token is returned
    //             .andReturn();

    //     String token = objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    //     assertNotNull(token); // Ensure token is not null
    // }
}
