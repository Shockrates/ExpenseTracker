package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import com.sokratis.ExpenseTracker.DTO.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Model.Category;


public class CategoryMapper {
    
    private static final ModelMapper modelMapper = new ModelMapper();
    
    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        // List<ExpenseDTO> expenseDTOList = category.getExpenses().stream()
        //                                      .map(ExpenseMapper::toDTO)
        //                                      .collect(Collectors.toList());
        // categoryDTO.setExpenses(expenseDTOList);
        return categoryDTO;
    }

    public static CategoryDTO toDTO(Category category, List<ExpenseDTO> expenseDTOList) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        // List<ExpenseDTO> expenseDTOList = category.getExpenses().stream()
        //                                      .map(ExpenseMapper::toDTO)
        //                                      .collect(Collectors.toList());
        // categoryDTO.setExpenses(expenseDTOList);
        return categoryDTO;
    }

    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
            .map(CategoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    // @Override
    // public Category toEntity(CategoryDTO categoryDTO) {
    //     if (categoryDTO == null) {
    //         return null;
    //     }
    //     Category category = super.toEntity(categoryDTO);
    //     List<Expense> expenseList = categoryDTO.getExpenses().stream()
    //                                        .map(ExpenseMapper::toEntity())
    //                                        .collect(Collectors.toList());
    //     user.setAddresses(addressList);
    //     return user;
    // }

}
