package com.sokratis.ExpenseTracker.DTO.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sokratis.ExpenseTracker.Model.Expense;

// public class ExpenseDTO {

//     private Long expenseId;
//     private Double expenseAmount;
//     private LocalDate expenseDate;
//     private String expenseDescription;
//     private Long categoryId;
//     private String categoryName;
//     private Long paidById;
//     private String paidByName;
//     // private CategoryDTO expenseCategory;
//     // private UserDTO expenseUser;

// }

public record ExpenseDTO(
                Long expenseId,
                BigDecimal expenseAmount,
                LocalDate expenseDate,
                String expenseDescription,
                CategorySummary categorySummary,
                UserSummary userSummary) {

        public record CategorySummary(Long categoryId, String categoryName, String color) {
        }

        public record UserSummary(Long userId, String userName) {
        }

        public ExpenseDTO(Expense expense) {
                this(
                                expense.getExpenseId(),
                                expense.getExpenseAmount(),
                                expense.getExpenseDate(),
                                expense.getExpenseDescription(),
                                new CategorySummary(
                                                expense.getExpenseCategory().getCategoryId(),
                                                expense.getExpenseCategory().getCategoryName(),
                                                expense.getExpenseCategory().getColor()),
                                new UserSummary(
                                                expense.getExpenseUser().getUserId(),
                                                expense.getExpenseUser().getUserName()));
        }
}
