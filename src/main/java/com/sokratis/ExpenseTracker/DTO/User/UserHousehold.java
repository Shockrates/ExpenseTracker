package com.sokratis.ExpenseTracker.DTO.User;

import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;

public record UserHousehold(
    Long id,
    List<HouseholdDTO> userHouseholds
) {
    
}
