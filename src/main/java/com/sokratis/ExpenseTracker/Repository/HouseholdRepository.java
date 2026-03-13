package com.sokratis.ExpenseTracker.Repository;

import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.Household;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @EntityGraph(attributePaths = {"createdBy"})
    List<Household> findAllWithCreatedBy();
}
