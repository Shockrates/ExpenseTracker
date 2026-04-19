package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDetailedDTO;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Household;

public class CategoryMapper extends EntityMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return new CategoryDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getColor(),
                category.getBudgetLimit(),
                CategoryMapper.getHouseholdId(category));
    }

    public static CategoryDetailedDTO toDetailedDTO(Category category, List<ExpenseDTO> expenseDTOList) {
        if (category == null) {
            return null;
        }

        return new CategoryDetailedDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getColor(),
                category.getBudgetLimit(),
                CategoryMapper.getHouseholdId(category),
                expenseDTOList.stream().mapToDouble(ExpenseDTO::getExpenseAmount).sum(),
                expenseDTOList);
    }

    public static Category toEntity(CategoryCreationRequest request, Household household) {
        Category category = new Category();
        category.setCategoryName(request.categoryName());
        category.setColor(request.color());
        category.setBudgetLimit(request.budgetLimit());
        category.setHousehold(household);

        return category;
    }

    public static List<CategoryDTO> toDTOList(List<Category> clist) {
        return clist.stream()
                .map(category -> new CategoryDTO(
                        category.getCategoryId(),
                        category.getCategoryName(),
                        category.getColor(),
                        category.getBudgetLimit(),
                        CategoryMapper.getHouseholdId(category)))
                .toList();

    }

    public static Long getHouseholdId(Category category) {
        return category.getHousehold() != null
                ? category.getHousehold().getId()
                : null;
    }

}
