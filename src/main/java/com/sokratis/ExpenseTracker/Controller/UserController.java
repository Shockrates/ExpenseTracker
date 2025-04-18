package com.sokratis.ExpenseTracker.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ApiResponse;
import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.DTO.UserCreationRequest;
import com.sokratis.ExpenseTracker.Model.User;

import com.sokratis.ExpenseTracker.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    @Operation(summary = "Get all User", description = "Fetch a list of all Users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){

        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Fetch a single user by its ID")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id){
 
        return userService.fetchUser(id)
        .map(user -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Found", user)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id )));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update existing User", description = "Update a User's details")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserCreationRequest user) {
 
            return userService.updateUser(id, user)
                    .map(updatedUser -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Updated", updatedUser)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id )));
      
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update an existing User's Password", description = "Update a User's password")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
    
        String newPassword = request.get("userPassword");
        try {
            userService.updatePassword(id, newPassword);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Password updated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id ));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Remove a user from the system")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
     
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   
    }

    @GetMapping("/{id}/total")
    @Operation(summary = "Get the total of a User", description = "Get the total ammount of all expense made by a single User")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<Double>> getUserTotalExpenseAmount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Total amount of user is", userService.calculateTotalbyUser(id)));
    }


}
