package com.sokratis.ExpenseTracker.DTO.Household;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
    
    @NotNull(message = "Email cannot be null") 
    @Email(message = "Invalid email format")
    String email
) {

}
