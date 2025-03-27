package com.sokratis.ExpenseTracker.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseListDTO;
import com.sokratis.ExpenseTracker.Exceptions.ResourceNotFoundException;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;
import com.sokratis.ExpenseTracker.Service.Interfaces.IExpenseService;
import com.sokratis.ExpenseTracker.utils.EntityUtils;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseService implements IExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // Retrive all Expenses
    public List<ExpenseDTO> fetchExpenseList(){  
        return ExpenseMapper.toDTOList(expenseRepository.findAllByOrderByExpenseDateDesc());  
    }

    // Retrive all Expenses with Total Amount
    public ExpenseListDTO fetchExpenseListWDetails(){  
        return ExpenseMapper.toExpenseListDTO(expenseRepository.findAll());  
    }

    // Retrieve an expense by ID
    public Optional<ExpenseDTO> fetchExpense(Long ExpenseId ){
        return expenseRepository.findById(ExpenseId)
        .map(ExpenseMapper::toDTO);
    }

     // Create a new expense
    public ExpenseDTO saveExpense(Expense expense) {
        
        User user = userRepository.findById(expense.getExpenseUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(expense.getExpenseCategory().getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        expense.setExpenseUser(user);
        expense.setExpenseCategory(category);

        return ExpenseMapper.toDTO(expenseRepository.save(expense));
    }

    // Update an existing expense
    public Optional<ExpenseDTO> updateExpense(Long ExpenseId, Expense updatedExpense){

        return expenseRepository.findById(ExpenseId).map(expense -> {

            BeanUtils.copyProperties(updatedExpense, expense, EntityUtils.getNullPropertyNames(updatedExpense));
            
            if (updatedExpense.getExpenseUser() != null && userRepository.existsById(updatedExpense.getExpenseUser().getUserId())) {
                expense.setExpenseUser(updatedExpense.getExpenseUser());
            } else throw new ResourceNotFoundException("User not found");

            if (updatedExpense.getExpenseCategory() != null && categoryRepository.existsById(updatedExpense.getExpenseCategory().getCategoryId())) {
                expense.setExpenseCategory(updatedExpense.getExpenseCategory());
            } else throw new ResourceNotFoundException("Category not found");


            return ExpenseMapper.toDTO(
                expenseRepository.save(expense)
            );
        });
    }

    public void deleteExpenseById(Long ExpenseId){
        if (expenseRepository.existsById(ExpenseId)) {
            expenseRepository.deleteById(ExpenseId);
        } else {
            throw new ResourceNotFoundException("Expense not found with id: " + ExpenseId);
        }
    }

    public List<ExpenseDTO> fetchExpensesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        return ExpenseMapper.toDTOList(expenseRepository.findByExpenseUser(user)); 
    }

    public List<ExpenseDTO> fetchExpensesByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return ExpenseMapper.toDTOList(expenseRepository.findByExpenseCategory(category)); 
    }

    public List<ExpenseDTO> fetchExpensesBetweenDates(LocalDate startDate, LocalDate endDate) {

        return ExpenseMapper.toDTOList(expenseRepository.findByExpenseDateBetween(startDate, endDate)); 
    }

   
    public List<ExpenseDTO> fetchExpensesBetweenRanges(Double lowestAmount, Double highestAmount) {
        return ExpenseMapper.toDTOList(expenseRepository.findByExpenseAmountBetween(lowestAmount, highestAmount));
    }

    @Override
    public Double calculateTotalExpenseAmount() {
        
        return expenseRepository.getTotalExpenseAmount();
    }




}
