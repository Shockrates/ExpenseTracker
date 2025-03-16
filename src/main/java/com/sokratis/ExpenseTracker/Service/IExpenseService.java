package com.sokratis.ExpenseTracker.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sokratis.ExpenseTracker.DTO.ExpenseDTO;
import com.sokratis.ExpenseTracker.Model.Category;
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
    Optional<ExpenseDTO> updateExpense(Long ExpenseId, Expense expense);

    /**
     * Deletes a Expense entity by its ID.
     * @param ExpenseId the ID of the Expense to delete
     */
    void deleteExpenseById(Long ExpenseId);

    /**
     * Fetches the list of all Expense entities made by a single User.
     * @param User the User of the Expense to fetch
     * @return the requested Expense List
     */
    public List<ExpenseDTO> fetchExpensesByUser(Long userId);

    /**
     * Fetches the list of all Expense entities of a single Category.
     * @param category the Category of the Expense to fetch
     * @return the requested Expense List
     */
    public List<ExpenseDTO> fetchExpensesByCategory(Long categoryId);


    /**
     * Fetches the list of all Expense entities made between two dates.
     * @param startDate the begin date
     * @param  endDate the end date
     * @return the requested Expense List
     */
    public List<ExpenseDTO> fetchExpensesBetweenDates(LocalDate startDate, LocalDate endDate);

     /**
     * Fetches the list of all Expense entities made with amounts between two ranges.
     * @param lowsetAmount the lowset amount exense
     * @param  highestAmount the highest amount exense
     * @return the requested Expense List
     */
    public List<ExpenseDTO> fetchExpensesBetweenRanges(Double lowestAmount, Double highest);


    /**
     * Fetches the total amount of all Expense entities.
     * @return  the Total of all Expenses
     */
    public Double  calculateTotalExpenseAmount();

}
