package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDetailedDTO;
import com.sokratis.ExpenseTracker.Mapper.CategoryMapper;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Mapper.HouseholdMapper;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;
import com.sokratis.ExpenseTracker.Repository.HouseholdRepository;
import com.sokratis.ExpenseTracker.Service.Interfaces.ICategoryService;
import com.sokratis.ExpenseTracker.utils.EntityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final HouseholdRepository householdRepository;

    @Override
    public List<CategoryDTO> fetchCategoryList() {
        // return categoryRepository.findAll().stream()
        // .map(category -> new CategoryDTO(
        // category.getCategoryId(),
        // category.getCategoryName(),
        // CategoryMapper.getHouseholdId(category),
        // null))
        // .toList();
        return CategoryMapper.toDTOList(categoryRepository.findAll());
    }

    @Override
    public Optional<CategoryDTO> fetchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> CategoryMapper.toDTO(category, CategoryDTO.class));
    }

    @Override
    public CategoryDTO saveCategory(CategoryCreationRequest category) {

        // if
        // (categoryRepository.findByCategoryNameIgnoreCase(category.categoryName()).isPresent())
        // {
        // throw new IllegalArgumentException("Category with the same name already
        // exists!");
        // }
        if (categoryRepository.existsByCategoryNameIgnoreCaseAndHousehold_Id(category.categoryName(),
                category.householdId())) {
            throw new IllegalArgumentException("Category with the same name already exists!");
        }

        Household household = householdRepository.getReferenceById(category.householdId());
        Category savedCategory = categoryRepository.save(CategoryMapper.toEntity(category, household));
        return CategoryMapper.toDTO(savedCategory);
    }

    @Override
    public Optional<Category> updateCategory(Long categoryId, CategoryCreationRequest updatedCategory) {

            // Optional<Category> category = categoryRepository.findById(categoryId);

            // if (category.isEmpty()) {
            //   throw new IllegalArgumentException("Wrong Category Id");  
            // }
          if (categoryRepository.existsByCategoryNameIgnoreCaseAndHousehold_Id(updatedCategory.categoryName(),
                updatedCategory.householdId())) {
            throw new IllegalArgumentException("Category with the same name already exists!");
        }

        //NEEDS UPDATE
        return categoryRepository.findById(categoryId).map(category -> {
            Optional<Category> existingCategoryWithName = categoryRepository
                    .findByCategoryNameIgnoreCase(updatedCategory.categoryName());
            if (existingCategoryWithName.isPresent()
                    && !existingCategoryWithName.get().getCategoryId().equals(categoryId)) {
                throw new IllegalArgumentException("Category with the same name already exists!");
            }

            BeanUtils.copyProperties(updatedCategory, category, EntityUtils.getNullPropertyNames(updatedCategory));

            return categoryRepository.save(category);
        });

    }
    @Override
    public void deleteCategoryById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("category not found!");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Double calculateTotalbyCategory(Long CategoryId) {
        return expenseRepository.getTotalExpensesByCategory(CategoryId);
    }

    public CategoryDetailedDTO fetchCategoryWithExpenses(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<ExpenseDTO> expenseDTOs = ExpenseMapper.toDTOList(expenseRepository.findByExpenseCategory(category));

        return CategoryMapper.toDetailedDTO(category, expenseDTOs);
        // return new CategoryDTO(
        // category.getCategoryId(),
        // category.getCategoryName(),
        // category.getColor(),
        // category.getBudgetLimit(),
        // CategoryMapper.getHouseholdId(category));
    }

}
