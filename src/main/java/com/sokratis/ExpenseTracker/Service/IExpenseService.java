package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Model.Expense;

public interface IExpenseService {

   

    /**
     * Fetches the list of all Expense entities.
     * @return a list of Expenses
     */
    List<ExpenseDTO> fetchExpenseList();

     /**
     * Fetches an Expense entity.
     * @param ExpenseId the ID of the Expense to fetch
     * @return the requested Expense
     */
    Optional<ExpenseDTO> fetchExpense(Long ExpenseId );

     /**
     * Saves a Expense entity.
     * @param Expense the Expense to save
     * @return the saved Expense
     */
    ExpenseDTO saveExpense(Expense expense);

    /**
     * Updates an existing Expense entity.
     * @param Expense the Expense with updated information
     * @param ExpenseId the ID of the Expense to update
     * @return the updated Expense
     */
    Expense updateExpense(Expense expense, Long ExpenseId);

    /**
     * Deletes a Expense entity by its ID.
     * @param ExpenseId the ID of the Expense to delete
     */
    void deleteExpenseById(Long ExpenseId);

}
