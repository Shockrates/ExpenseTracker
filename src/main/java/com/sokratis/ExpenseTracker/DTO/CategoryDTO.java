package com.sokratis.ExpenseTracker.DTO;

import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.Model.Household;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private HouseholdDTO household;
    private List<ExpenseDTO> expenses;

}
