package com.sokratis.ExpenseTracker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.HouseholdMember;

@Repository
public interface HoseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {

    List<HouseholdMember> findByUserId(Long userId);

    List<HouseholdMember> findByHouseholdId(Long householdId);

}
