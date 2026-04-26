package com.sokratis.ExpenseTracker.DTO.Expense;

import java.time.LocalDate;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.User.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDetailedDTO {
    private Long expenseId;
    private Double expenseAmount;
    private LocalDate expenseDate;
    private String expenseDescription;
    private CategoryDTO expenseCategory;
    private UserDTO expenseUser;

}
