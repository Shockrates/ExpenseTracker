package com.sokratis.ExpenseTracker.DTO;

import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;


public record CategoryDTO(
    Long id,
    String name,
    HouseholdDTO household,
    List<ExpenseDTO> expenses
) 
 {}
