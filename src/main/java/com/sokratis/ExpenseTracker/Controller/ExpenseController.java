package com.sokratis.ExpenseTracker.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import static org.springframework.http.ResponseEntity.status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Service.ExpenseService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {
    
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.fetchExpenseList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long id){
 
        // return expenseService.fetchExpense(id)
        //     .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        //     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        return expenseService.fetchExpense(id)
        .map(expense -> ResponseEntity.status(HttpStatus.OK).body(expense))
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody Expense expense) {
        
        ExpenseDTO createdExpense = expenseService.saveExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }
}
