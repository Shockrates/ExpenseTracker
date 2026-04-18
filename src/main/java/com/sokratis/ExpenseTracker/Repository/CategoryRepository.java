package com.sokratis.ExpenseTracker.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryTotalDTO;
import com.sokratis.ExpenseTracker.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = { "household" })
    List<Category> findAll();

    Optional<Category> findByCategoryNameIgnoreCase(String name);

    boolean existsByCategoryNameIgnoreCaseAndHousehold_Id(String categoryName, Long householdId);

    boolean existsByCategoryId(Long categoryId);

    List<Category> findByHouseholdId(Long householdId);

    @Query("""
            SELECT
            new com.sokratis.ExpenseTracker.DTO.Category.CategoryTotalDTO(
                c.id,
                c.categoryName,
                c.color,
                c.budgetLimit,
                c.household.id,
                COALESCE(SUM(e.expenseAmount), 0)
            )
            FROM Category c
            LEFT JOIN Expense e
                ON e.expenseCategory = c
                AND e.expenseDate BETWEEN :startDate AND :endDate
            WHERE c.household.id = :householdId
            GROUP BY c
                                """)
    List<CategoryTotalDTO> findCategoriesWithExpenseTotalsByHouseholdId(
            @Param("householdId") Long householdId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
