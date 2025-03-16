package com.sokratis.ExpenseTracker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.User;


public interface CategoryRepository extends JpaRepository<Category, Long>{

    Optional<Category> findByCategoryNameIgnoreCase(String name);

}
