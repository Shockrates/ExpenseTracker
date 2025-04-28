package com.sokratis.ExpenseTracker.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Expense> findAllByOrderByExpenseDateDesc( Pageable pageable);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    Page<Expense> findByExpenseUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    Page<Expense> findByExpenseCategory(Category category,Pageable pageable);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseCategory(Category category);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    Page<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    Page<Expense> findByExpenseAmountBetween(Double lowestAmount, Double highestAmount, Pageable pageable);

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e")
    Double getTotalExpenseAmount();

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.expenseUser.id = :userId")
    Double getTotalExpensesByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(e.expenseAmount), 0) FROM Expense e WHERE e.expenseCategory.id = :categoryId")
    Double getTotalExpensesByCategory(@Param("categoryId") Long categoryId);
    
}
