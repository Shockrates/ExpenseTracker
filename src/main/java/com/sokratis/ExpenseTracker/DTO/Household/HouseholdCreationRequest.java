package com.sokratis.ExpenseTracker.DTO.Household;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record HouseholdCreationRequest(

    @NotNull(message = "Household name cannot be null")
    @NotEmpty(message = "Name may not be empty")
    String name
) {
    
   
}
