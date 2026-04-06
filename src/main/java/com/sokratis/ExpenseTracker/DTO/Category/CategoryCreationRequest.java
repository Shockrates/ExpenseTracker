package com.sokratis.ExpenseTracker.DTO.Category;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record CategoryCreationRequest(

        @NotNull(message = "Category name cannot be null") String categoryName,
        @NotNull(message = "Please provide a display color") String color,
        BigDecimal budgetLimit,
        Long householdId) {

}

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
// public class CategoryCreationRequest {

// @NotNull(message = "Category name cannot be null")
// private String categoryName;

// @NotNull(message = "Please provide a display color")
// private String color;

// private BigDecimal budgetLimit;
// private Long householdId;
// }