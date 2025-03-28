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
import com.sokratis.ExpenseTracker.Exceptions.ResourceNotFoundException;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/expenses")
@Tag(name = "Expense Controller", description = "APIs for managing expenses")
public class ExpenseController {
    
    private final ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Get all Expenses", description = "Fetch a list of all Expenses")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpenses(){
        System.out.println("FIND ALL");
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense List", expenseService.fetchExpenseList()) );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Expense by ID", description = "Fetch a single expense by its ID")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpense(@PathVariable Long id){
 
        return expenseService.fetchExpense(id)
        .map(expense -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense Found", expense)))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id )));
    }

    @PostMapping
    @Operation(summary = "Create a new expense", description = "Add a new expense to the system")
    public ResponseEntity<ApiResponse<ExpenseDTO>> createExpense(@Valid @RequestBody Expense expense) {
        
        try {
            ExpenseDTO createdExpense = expenseService.saveExpense(expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Expense Created", createdExpense));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing expense", description = "Update a expense's details")
    @PreAuthorize("@securityUtils.isOwnerOfExpense(#id)")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        try {
            
            return expenseService.updateExpense(id, expense)
                    .map(updatedExpense -> ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Expense Updated", updatedExpense)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id )));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a expense", description = "Remove a expense from the system")
    @PreAuthorize("@securityUtils.isOwnerOfExpense(#id)")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Expense not found with id "+id ));
        }
    }

    //CUSTOM ENDPOINTS

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all Expenses by User", description = "Fetch a list of all Expenses made by a specific User ")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByUser(@PathVariable Long userId){
        
        try {
            List<ExpenseDTO> expenses = expenseService.fetchExpensesByUser(userId);
            String message = expenses.isEmpty() ? "No Expenses for this User" : "List of Expenses by User " + expenses.get(0).getUserName();
            System.out.println(expenses);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all Expenses by Category", description = "Fetch a list of all Expenses of specific category ")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByCategory(@PathVariable Long categoryId){
        
        try {
            List<ExpenseDTO> expenses = expenseService.fetchExpensesByCategory(categoryId);
            String message = expenses.isEmpty() ? "No Expenses for this Category" : "List of Expenses by Category "+ expenses.get(0).getCategoryName();
            System.out.println(expenses);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/between-dates")
    @Operation(summary = "Get all Expenses between two dates", description = "Fetch a list of all Expenses between two dates")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenDates(
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        List<ExpenseDTO> expenses = expenseService.fetchExpensesBetweenDates(startDate, endDate);
        String message = expenses.isEmpty() ? "No Expenses for this Time Range" : "List of Expenses between "+ startDate + " and " + endDate;

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
    }

    @GetMapping("/between-ranges")
    @Operation(summary = "Get all Expenses between two price ranges", description = "Fetch a list of all Expenses between two price ranges")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesBetweenRanges(@RequestParam Double lowestAmount, @RequestParam Double highestAmount) {

        List<ExpenseDTO> expenses = expenseService.fetchExpensesBetweenRanges(lowestAmount, highestAmount);
        String message = expenses.isEmpty() ? "No Expenses for this price Range" : "List of Expenses between "+ lowestAmount + " and " + highestAmount;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message, expenses));
    }

    @GetMapping("/total")
    @Operation(summary = "Get the total", description = "Get the total ammount of all expense")
    public ResponseEntity<ApiResponse<Double>> getTotalExpenseAmount(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Total amount is", expenseService.calculateTotalExpenseAmount()));
    }

        
}
