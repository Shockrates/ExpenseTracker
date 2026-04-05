package com.sokratis.ExpenseTracker.DTO.Category;

import java.math.BigDecimal;
import java.util.List;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;

public record CategoryDetailedDTO(
        Long id,
        String name,
        String color,
        BigDecimal budgetLimit,
        Long householdId,
        List<ExpenseDTO> expenses) {
}
