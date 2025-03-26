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
import com.sokratis.ExpenseTracker.Model.User;

import com.sokratis.ExpenseTracker.Service.UserService;


import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User user){
     
       
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Logged In", userService.verifyUser(user))) ;
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){

        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id){
 
        return userService.fetchUser(id)
        .map(user -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Found", user)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id )));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody User user) {
        
        try {
            UserDTO createdUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User Created", createdUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto) {
        try {
            
            return userService.updateUser(id, userDto)
                    .map(updatedUser -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("User Updated", updatedUser)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id )));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/password")
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
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("User not found with id "+id ));
        }
    }

    @GetMapping("/{id}/total")
    @PreAuthorize("hasRole('ADMIN') or #id == @securityUtils.getPrincipalId()")
    public ResponseEntity<ApiResponse<Double>> getUserTotalExpenseAmount(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Total amount of user is", userService.calculateTotalbyUser(id)));
    }


}
