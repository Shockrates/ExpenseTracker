package com.sokratis.ExpenseTracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {

    @NotNull(message = "User name cannot be null")
    private String userName;

    @NotNull(message = "Email cannot be null") // Application-level validation
    @Email(message = "Invalid email format") // Application-level validation
    private String userEmail;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String userPassword;
}
