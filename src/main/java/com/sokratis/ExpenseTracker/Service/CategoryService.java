package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public List<Category> fetchCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> fetchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
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

            return categoryRepository.save(updatedCategory);
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
        return expenseRepository.getTotalExpensesByUser(CategoryId);
    }

    
}
