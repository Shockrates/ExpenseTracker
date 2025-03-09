package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;

import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseService implements IExpenseService{

    private final ExpenseRepository expenseRepository;

    public List<ExpenseDTO> fetchExpenseList(){
        
        return ExpenseMapper.toDTOList(expenseRepository.findAll());
        
    }

    public Optional<ExpenseDTO> fetchExpense(Long ExpenseId ){
        //Expense expense = expenseRepository.findById(ExpenseId).get();
        return expenseRepository.findById(ExpenseId)
        .map(ExpenseMapper::toDTO);
    }

    public ExpenseDTO saveExpense(Expense expense) {
        // Set created_at to the current timestamp
        //expense.setCreatedAt(java.time.LocalDate.now());

        return ExpenseMapper.toDTO(expenseRepository.save(expense));
    }

    public Expense updateExpense(Expense expense, Long ExpenseId){

        if (expenseRepository.existsById(ExpenseId)) {
            expense.setExpenseId(ExpenseId);

            // Set updated_at to the current timestamp
            expense.setUpdatedAt(java.time.LocalDate.now());

            return expenseRepository.save(expense);
        }
        throw new RuntimeException("Expense not found with id: " + ExpenseId);
    }

    public void deleteExpenseById(Long ExpenseId){
        if (expenseRepository.existsById(ExpenseId)) {
            expenseRepository.deleteById(ExpenseId);
        } else {
            throw new RuntimeException("Expense not found with id: " + ExpenseId);
        }
    }



}
