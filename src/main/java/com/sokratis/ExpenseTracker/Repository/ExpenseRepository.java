package com.sokratis.ExpenseTracker.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{

    List<Expense> findByUser(User user);

    List<Expense> findByExpenseCategory(Category category);

    List<Expense> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    List<Expense> findByExpenseAmountBetween(Double start, Double end);
}
