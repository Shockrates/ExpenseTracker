package com.sokratis.ExpenseTracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sokratis.ExpenseTracker.Model.Category;


public interface CategoryRepository extends JpaRepository<Category, Long>{

}
