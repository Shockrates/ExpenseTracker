package com.sokratis.ExpenseTracker.DTO;

import java.time.LocalDate;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.User.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {

    private Long expenseId;
    private Double expenseAmount;
    private LocalDate expenseDate;
    private String expenseDescription;
    private Long categoryId;
    private String categoryName;
    private Long paidById;
    private String paidByName;
    // private CategoryDTO expenseCategory;
    // private UserDTO expenseUser;

}
