package com.sokratis.ExpenseTracker.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseListDTO {

    private List<ExpenseDTO> expenseList;
    private Double expenseAmountTotal;

}
