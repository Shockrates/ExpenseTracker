package com.sokratis.ExpenseTracker.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ApiResponse;
import com.sokratis.ExpenseTracker.DTO.LoginRequest;
import com.sokratis.ExpenseTracker.DTO.UserCreationRequest;
import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthContorller {
    
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login a user")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest user){
     
       
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Logged In", userService.verifyUser(user))) ;
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register a User", description = "Add a new User to the system")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserCreationRequest user) {
        
        try {
            UserDTO createdUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User Created", createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        
        // Extract token from request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Remove "Bearer " prefix
        }
;
        if (token != null) {
            // Invalidate the token
            userService.logout(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(400).body("Invalid token");
    }
}
