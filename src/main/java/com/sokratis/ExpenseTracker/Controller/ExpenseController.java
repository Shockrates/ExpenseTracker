package com.sokratis.ExpenseTracker.Controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
//import static org.springframework.http.ResponseEntity.status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpenses(){
        System.out.println("FIND ALL");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense List", expenseService.fetchExpenseList()) );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpense(@PathVariable Long id){
 
        return expenseService.fetchExpense(id)
        .map(expense -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense Found", expense)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id )));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> createExpense(@RequestBody Expense expense) {
        
        try {
            ExpenseDTO createdExpense = expenseService.saveExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Expense Created", createdExpense));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityUtils.isOwnerOfExpense(#id)")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        try {
            
            return expenseService.updateExpense(id, expense)
                    .map(updatedExpense -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense Updated", updatedExpense)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id )));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id ));
        }
    }

    //CUSTOM ENDPOINTS

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByUser(@PathVariable Long userId){
        
        try {
            List<ExpenseDTO> expenses = expenseService.fetchExpensesByUser(userId);
            String message = expenses.isEmpty() ? "No Expenses for this User" : "List of Expenses by User " + expenses.get(0).getUserName();
            System.out.println(expenses);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByCategory(@PathVariable Long categoryId){
        
        try {
            List<ExpenseDTO> expenses = expenseService.fetchExpensesByCategory(categoryId);
            String message = expenses.isEmpty() ? "No Expenses for this Category" : "List of Expenses by Category "+ expenses.get(0).getCategoryName();
            System.out.println(expenses);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/between-dates")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenDates(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        List<ExpenseDTO> expenses = expenseService.fetchExpensesBetweenDates(startDate, endDate);
        String message = expenses.isEmpty() ? "No Expenses for this Time Range" : "List of Expenses between "+ startDate + " and " + endDate;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
    }

    @GetMapping("/between-ranges")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenRanges(@RequestParam Double lowestAmount, @RequestParam Double highestAmount) {

        List<ExpenseDTO> expenses = expenseService.fetchExpensesBetweenRanges(lowestAmount, highestAmount);
        String message = expenses.isEmpty() ? "No Expenses for this price Range" : "List of Expenses between "+ lowestAmount + " and " + highestAmount;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpenseAmount(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Total amount is", expenseService.calculateTotalExpenseAmount()));
    }

        
}
