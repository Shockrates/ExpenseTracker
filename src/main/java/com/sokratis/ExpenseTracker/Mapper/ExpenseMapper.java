package com.sokratis.ExpenseTracker.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.Expense.ExpenseCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Expense.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.Expense.ExpenseDetailedDTO;
import com.sokratis.ExpenseTracker.DTO.Expense.ExpenseListDTO;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;

public class ExpenseMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ExpenseDTO toDTO(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseDTO.UserSummary user = null;
        if (expense.getExpenseUser() != null) {
            user = new ExpenseDTO.UserSummary(
                    expense.getExpenseUser().getUserId(),
                    expense.getExpenseUser().getUserName());
        }

        ExpenseDTO.CategorySummary category = null;
        if (expense.getExpenseCategory() != null) {
            category = new ExpenseDTO.CategorySummary(
                    expense.getExpenseCategory().getCategoryId(),
                    expense.getExpenseCategory().getCategoryName(),
                    expense.getExpenseCategory().getColor());
        }

        return new ExpenseDTO(
                expense.getExpenseId(),
                expense.getExpenseAmount(),
                expense.getExpenseDate(),
                expense.getExpenseDescription(),
                category,
                user);
    }

    public static ExpenseDetailedDTO toDetailedDTO(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseDetailedDTO dto = modelMapper.map(expense, ExpenseDetailedDTO.class);

        if (expense.getExpenseUser() != null) {
            dto.setExpenseUser(UserMapper.toDTO(expense.getExpenseUser()));

        }

        if (expense.getExpenseCategory() != null) {
            dto.setExpenseCategory(CategoryMapper.toDTO(expense.getExpenseCategory()));
        }

        return dto;
    }

    public static List<ExpenseDTO> toDTOList(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Expense toEntity(ExpenseDetailedDTO dto) {
        if (dto == null) {
            return null;
        }

        Expense expense = modelMapper.map(dto, Expense.class);
        expense.setExpenseUser(UserMapper.toEntity(dto.getExpenseUser()));
        expense.setExpenseCategory(CategoryMapper.toEntity(dto.getExpenseCategory(), Category.class));

        return expense;
    }

    public static Expense toEntity(ExpenseCreationRequest request) {
        if (request == null) {
            return null;
        }

        Expense expense = modelMapper.map(request, Expense.class);
        return expense;
    }

    // public static List<Expense> toEntityListWithSameUser(List<ExpenseDTO>
    // dtoList) {
    // return dtoList.stream()
    // .map(dto -> toEntity(dto))
    // .collect(Collectors.toList());
    // }

    public static ExpenseListDTO toExpenseListDTO(List<Expense> expenses) {

        List<ExpenseDTO> expenseDTOs = ExpenseMapper.toDTOList(expenses);
        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getExpenseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseListDTO(expenseDTOs, totalAmount);

    }

    public static Page<ExpenseDTO> toExpenseDTOPage(Page<Expense> expenses) {
        // return expenses.map(expense -> modelMapper.map(expense, ExpenseDTO.class));
        return expenses.map(ExpenseMapper::toDTO);

    }

}
