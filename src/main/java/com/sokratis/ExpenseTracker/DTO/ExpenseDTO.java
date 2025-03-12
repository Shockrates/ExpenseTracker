package com.sokratis.ExpenseTracker.DTO;

import java.time.LocalDate;
import java.util.Date;

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
    private String categoryName;
    private String userName;

}

