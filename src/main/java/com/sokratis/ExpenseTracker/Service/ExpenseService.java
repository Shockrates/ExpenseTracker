package com.sokratis.ExpenseTracker.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseListDTO;
import com.sokratis.ExpenseTracker.DTO.ExpenseCreationRequest;
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
    public Page<ExpenseDTO> fetchExpenseList(int page, int size){  
        return ExpenseMapper.toExpenseDTOPage(expenseRepository.findAllByOrderByExpenseDateDesc(PageRequest.of(page, size)));  
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
    public ExpenseDTO saveExpense(ExpenseCreationRequest expenseRequest) {
        
        User user = userRepository.findById(expenseRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(expenseRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Expense expense =  ExpenseMapper.toEntity(expenseRequest);
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

    public Page<ExpenseDTO> fetchExpensesByUser(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        return ExpenseMapper.toExpenseDTOPage(expenseRepository.findByExpenseUser(user,PageRequest.of(page, size))); 
    }

    public Page<ExpenseDTO> fetchExpensesByCategory(Long categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return ExpenseMapper.toExpenseDTOPage(expenseRepository.findByExpenseCategory(category,PageRequest.of(page, size))); 
    }

    public Page<ExpenseDTO> fetchExpensesBetweenDates(LocalDate startDate, LocalDate endDate, int page, int size) {

        return ExpenseMapper.toExpenseDTOPage(expenseRepository.findByExpenseDateBetween(startDate, endDate, PageRequest.of(page, size))); 
    }

   
    public Page<ExpenseDTO> fetchExpensesBetweenRanges(Double lowestAmount, Double highestAmount, int page, int size) {
        return ExpenseMapper.toExpenseDTOPage(expenseRepository.findByExpenseAmountBetween(lowestAmount, highestAmount, PageRequest.of(page, size)));
    }

    @Override
    public Double calculateTotalExpenseAmount() {
        
        return expenseRepository.getTotalExpenseAmount();
    }




}
