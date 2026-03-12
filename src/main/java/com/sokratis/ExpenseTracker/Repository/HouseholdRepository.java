package com.sokratis.ExpenseTracker.Repository;

import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
}
