package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.sokratis.ExpenseTracker.DTO.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseListDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseCreationRequest;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;


public class ExpenseMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ExpenseDTO toDTO(Expense expense) {
        if (expense == null) {
            return null;
        }
        
        ExpenseDTO dto = modelMapper.map(expense, ExpenseDTO.class);
        
        if (expense.getExpenseUser() != null) {
            dto.setExpenseUser(UserMapper.toDTO(expense.getExpenseUser()));
        }
        
        if (expense.getExpenseCategory() != null) {
            dto.setExpenseCategory(CategoryMapper.toDTO(expense.getExpenseCategory(), CategoryDTO.class));
        }
        
        return dto;
    }
    
    public static List<ExpenseDTO> toDTOList(List<Expense> expenses) {
        return expenses.stream()
            .map(ExpenseMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public static Expense toEntity(ExpenseDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Expense expense = modelMapper.map(dto, Expense.class);
        expense.setExpenseUser(UserMapper.toEntity(dto.getExpenseUser()));
        expense.setExpenseCategory(CategoryMapper.toEntity(dto.getExpenseCategory(), Category.class));
        
        return expense;
    }

    public static Expense toEntity(ExpenseCreationRequest request) {
        if (request== null) {
            return null;
        }
        
        Expense expense = modelMapper.map(request, Expense.class);
        return expense;
    }


    
    public static List<Expense> toEntityListWithSameUser(List<ExpenseDTO> dtoList) {
        return dtoList.stream()
            .map(dto -> toEntity(dto))
            .collect(Collectors.toList());
    }

    public static ExpenseListDTO toExpenseListDTO(List<Expense> expenses){

        List<ExpenseDTO> expenseDTOs = ExpenseMapper.toDTOList(expenses);
        double totalAmount = expenses.stream()
                .mapToDouble(Expense::getExpenseAmount)
                .sum();
        
        return new ExpenseListDTO(expenseDTOs, totalAmount);

    }

    public static Page<ExpenseDTO> toExpenseDTOPage(Page<Expense> expenses) {
        return expenses.map(expense -> modelMapper.map(expense, ExpenseDTO.class));
    }

    
}
