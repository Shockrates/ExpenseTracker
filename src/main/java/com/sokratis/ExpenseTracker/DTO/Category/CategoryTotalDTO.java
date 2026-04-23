package com.sokratis.ExpenseTracker.DTO.Category;

import java.math.BigDecimal;

import com.sokratis.ExpenseTracker.Model.Category;

public record CategoryTotalDTO(
        Long id,
        String name,
        String color,
        BigDecimal budgetLimit,
        Long householdId,
        BigDecimal spent) {

        public CategoryTotalDTO(Category category, BigDecimal spent){
                this(category.getCategoryId(), category.getCategoryName(), category.getColor(), category.getBudgetLimit(), category.getHousehold().getId(), spent);
        }

}
