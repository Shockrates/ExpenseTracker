package com.sokratis.ExpenseTracker.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sokratis.ExpenseTracker.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    List<Category> findByHouseholdId(Long householdId);

}
