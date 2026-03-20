package com.sokratis.ExpenseTracker.Repository;

import org.springframework.stereotype.Repository;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.Model.Household;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @EntityGraph(attributePaths = { "createdBy" })
    Optional<Household> findWithCreatedById(Long id);

    @EntityGraph(attributePaths = { "createdBy" })
    List<Household> findAllWithCreatedBy();

    @Query("""
        SELECT new com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO(
            h.id,
            h.name,
            h.createdBy.userId,
            h.createdBy.userName,
            COUNT(hm.id)
        )
        FROM Household h
        LEFT JOIN HouseholdMember hm 
        ON hm.household = h
        WHERE EXISTS(
            SELECT 1
            FROM HouseholdMember hms
            WHERE hms.household=h
            AND hms.user.userId = :userId
        )
        GROUP BY h.id, h.name, h.createdBy.userId, h.createdBy.userName
    """)
    List<HouseholdDTO> findHouseholdDTOsForUser(@Param("userId") Long userId);
}
