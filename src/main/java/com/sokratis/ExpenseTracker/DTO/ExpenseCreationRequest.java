package com.sokratis.ExpenseTracker.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreationRequest {

    private Long expenseId;
    private Double expenseAmount;
    private LocalDate expenseDate;
    private String expenseDescription;
    private Long categoryId;
    private Long userId;
}
