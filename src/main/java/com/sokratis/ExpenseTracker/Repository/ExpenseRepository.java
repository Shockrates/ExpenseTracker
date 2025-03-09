package com.sokratis.ExpenseTracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{

}
