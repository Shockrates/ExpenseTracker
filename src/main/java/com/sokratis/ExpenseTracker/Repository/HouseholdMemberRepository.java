package com.sokratis.ExpenseTracker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.HouseholdMember;

@Repository
public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {

    List<HouseholdMember> findByUserUserId(Long userId);

    @EntityGraph(attributePaths = { "user" })
    List<HouseholdMember> findByHouseholdId(Long householdId);

}
