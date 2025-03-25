package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Mapper.CategoryMapper;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;
import com.sokratis.ExpenseTracker.Service.Interfaces.ICategoryService;
import com.sokratis.ExpenseTracker.utils.EntityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public List<CategoryDTO> fetchCategoryList() {
        return CategoryMapper.toDTOList( categoryRepository.findAll());
    }

    @Override
    public Optional<CategoryDTO> fetchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
        .map(CategoryMapper::toDTO);
    }

    @Override
    public Category saveCategory(Category category) {
        
        if (categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("Category with the same name already exists!");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> updateCategory(Long categoryId, Category updatedCategory) {
        
        return categoryRepository.findById(categoryId).map(category -> {
            Optional<Category> existingCategoryWithName = categoryRepository.findByCategoryNameIgnoreCase(updatedCategory.getCategoryName());
            if (existingCategoryWithName.isPresent() && !existingCategoryWithName.get().getCategoryId().equals(categoryId)) {
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

    
    public CategoryDTO fetchCategoryWithExpenses(Long categoryId) {
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
                
        List<ExpenseDTO> expenseDTOs = ExpenseMapper.toDTOList(expenseRepository.findByExpenseCategory(category));
                
        return new CategoryDTO(category.getCategoryId(), category.getCategoryName(), expenseDTOs);
    }

    

    
}
