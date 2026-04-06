package com.sokratis.ExpenseTracker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sokratis.ExpenseTracker.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = { "household" })
    List<Category> findAll();

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    boolean existsByCategoryNameIgnoreCaseAndHousehold_Id(String categoryName, Long householdId);

    List<Category> findByHouseholdId(Long householdId);

}
