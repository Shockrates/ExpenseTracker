package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;

public class ExpenseMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ExpenseDTO toDTO(Expense expense) {
        if (expense == null) {
            return null;
        }
        
        ExpenseDTO dto = modelMapper.map(expense, ExpenseDTO.class);
        
        if (expense.getExpenseUser() != null) {
            dto.setUserName(expense.getExpenseUser().getUserName());
        }
        
        if (expense.getExpenseCategory() != null) {
            dto.setCategoryName(expense.getExpenseCategory().getCategoryName());
        }
        
        return dto;
    }
    
    public static List<ExpenseDTO> toDTOList(List<Expense> expenses) {
        return expenses.stream()
            .map(ExpenseMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public static Expense toEntity(ExpenseDTO dto, User user, Category category) {
        if (dto == null) {
            return null;
        }
        
        Expense expense = modelMapper.map(dto, Expense.class);
        expense.setExpenseUser(user);
        expense.setExpenseCategory(category);
        
        return expense;
    }
    
    public static List<Expense> toEntityList(List<ExpenseDTO> dtoList, User user, Category category) {
        return dtoList.stream()
            .map(dto -> toEntity(dto, user, category))
            .collect(Collectors.toList());
    }
}
