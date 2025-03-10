package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Repository.CategoryRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseService implements IExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<ExpenseDTO> fetchExpenseList(){
        
        return ExpenseMapper.toDTOList(expenseRepository.findAll());
        
    }

    public Optional<ExpenseDTO> fetchExpense(Long ExpenseId ){
       
        return expenseRepository.findById(ExpenseId)
        .map(ExpenseMapper::toDTO);
    }

    public ExpenseDTO saveExpense(Expense expense) {
        
        User user = userRepository.findById(expense.getExpenseUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(expense.getExpenseCategory().getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        expense.setExpenseUser(user);
        expense.setExpenseCategory(category);

        return ExpenseMapper.toDTO(expenseRepository.save(expense));
    }

    public Optional<ExpenseDTO> updateExpense(Long ExpenseId, Expense updatedExpense){

        return expenseRepository.findById(ExpenseId).map(expense -> {
            
            updatedExpense.setExpenseId(expense.getExpenseId());
            Expense savedExpense = expenseRepository.save(updatedExpense);
            return ExpenseMapper.toDTO(savedExpense);
        });
    }

    public void deleteExpenseById(Long ExpenseId){
        if (expenseRepository.existsById(ExpenseId)) {
            expenseRepository.deleteById(ExpenseId);
        } else {
            throw new RuntimeException("Expense not found with id: " + ExpenseId);
        }
    }



}
