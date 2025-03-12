package com.sokratis.ExpenseTracker.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{

    @NonNull
    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findAll();

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseUser(User user);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseCategory(Category category);

    @EntityGraph(attributePaths = {"expenseUser", "expenseCategory"})
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);

    List<Expense> findByExpenseAmountBetween(Double start, Double end);
}
