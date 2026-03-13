package com.sokratis.ExpenseTracker.DTO.Household;

public record HouseholdDTO(
    Long id,
    String name,
    Long creatorId,
    String creatorName 
) {
    
}
