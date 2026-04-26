package com.sokratis.ExpenseTracker.DTO.Expense;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseListDTO {

    private List<ExpenseDTO> expenseList;
    private BigDecimal expenseAmountTotal;

}
