package com.sokratis.ExpenseTracker.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "User name cannot be null") 
    @Column(name = "user_name")
    private String userName;

    @NotNull(message = "Email cannot be null")  // Application-level validation
    @Email(message = "Invalid email format")  // Application-level validation
    @Column(name = "user_email")
    private String userEmail;

    @NotNull(message = "Password cannot be null") 
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Column(name = "user_password")
    private String userPassword;

    @Builder.Default
    @Column(name = "user_roles")
    private String userRoles ="USER";

    

}
