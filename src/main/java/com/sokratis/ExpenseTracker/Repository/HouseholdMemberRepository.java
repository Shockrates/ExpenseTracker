package com.sokratis.ExpenseTracker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.Model.HouseholdMember;
import com.sokratis.ExpenseTracker.Model.Enums.Role;

@Repository
public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {

    List<HouseholdMember> findByUserUserId(Long userId);

    @EntityGraph(attributePaths = { "user" })
    List<HouseholdMember> findByHouseholdId(Long householdId);

    Optional<HouseholdMember> findByHouseholdIdAndUserUserId(Long householdId, Long userId);

    boolean existsByHouseholdIdAndUserUserId(Long householdId, Long userId);

    boolean existsByHouseholdIdAndUserUserIdAndRole(Long householdId, Long userId, Role role);

    void deleteByHouseholdIdAndUserUserId(Long householdId, Long userId);

}
