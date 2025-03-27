package com.sokratis.ExpenseTracker.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{

    @NonNull
    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findAllByOrderByExpenseDateDesc();

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseUser(User user);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseCategory(Category category);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseAmountBetween(Double lowestAmount, Double highestAmount);

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e")
    Double getTotalExpenseAmount();

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.expenseUser.id = :userId")
    Double getTotalExpensesByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.expenseCategory.id = :categoryId")
    Double getTotalExpensesByCategory(@Param("categoryId") Long categoryId);
}
