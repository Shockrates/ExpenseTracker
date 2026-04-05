package com.sokratis.ExpenseTracker.DTO.Category;

import java.math.BigDecimal;

public record CategoryDTO(
        Long id,
        String name,
        String color,
        BigDecimal budgetLimit,
        Long householdId) {
}
