package com.sokratis.ExpenseTracker.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDetailedDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryTotalDTO;
import com.sokratis.ExpenseTracker.DTO.Expense.ExpenseDTO;
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

import jakarta.transaction.Transactional;
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

    public List<CategoryTotalDTO> fetchCategoryListWithTotals(Long householdId, LocalDate startDate,
            LocalDate endDate) {

        LocalDate now = LocalDate.now();
        if (startDate == null) {
            // startDate = now.withDayOfMonth(1);
            startDate = now.withDayOfYear(1);
        }
        if (endDate == null) {
            endDate = now;
        }
        return categoryRepository.findCategoriesWithExpenseTotalsByHouseholdId(householdId, startDate, endDate);
    }

    @Override
    public Optional<CategoryDTO> fetchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> CategoryMapper.toDTO(category));
    }

    public List<CategoryDTO> fetchCategoriesByHouseholdId(Long householdId) {
        return CategoryMapper.toDTOList(categoryRepository.findByHouseholdId(householdId));
    }

    @Override
    public CategoryTotalDTO saveCategory(CategoryCreationRequest category) {

        if (categoryRepository.existsByCategoryNameIgnoreCaseAndHousehold_Id(category.categoryName(),
                category.householdId())) {
            throw new IllegalArgumentException("Category with the same name already exists!");
        }

        Household household = householdRepository.getReferenceById(category.householdId());
        Category savedCategory = categoryRepository.save(CategoryMapper.toEntity(category, household));
        // return CategoryMapper.toDTO(savedCategory);
        return new CategoryTotalDTO(savedCategory, BigDecimal.ZERO);
    }

    @Transactional
    public CategoryDTO updateCategory(Long categoryId, CategoryCreationRequest updatedCategory) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category Not Found"));

        if (categoryRepository.existsByCategoryNameIgnoreCaseAndHousehold_Id(updatedCategory.categoryName(),
                category.getHousehold().getId())) {
            throw new IllegalArgumentException("Category with the same name already exists!");
        }

        BeanUtils.copyProperties(updatedCategory, category, EntityUtils.getNullPropertyNames(updatedCategory));

        try {
            return CategoryMapper.toDTO(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate category name: " + e.getMessage(), e);
        }

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

    public CategoryDetailedDTO fetchCategoryWithExpenses(Long categoryId, LocalDate startDate,
            LocalDate endDate) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        LocalDate now = LocalDate.now();
        if (startDate == null) {
            // startDate = now.withDayOfMonth(1);
            startDate = now.withDayOfYear(1);
        }
        if (endDate == null) {
            endDate = now;
        }
        // List<ExpenseDTO> expenseDTOs =
        // ExpenseMapper.toDTOList(expenseRepository.findByExpenseCategory(category));
        List<ExpenseDTO> expenseDTOs = ExpenseMapper
                .toDTOList(expenseRepository.findByExpenseCategoryAndExpenseDateBetween(category, startDate, endDate));

        return CategoryMapper.toDetailedDTO(category, expenseDTOs);
    }

}
