package com.sokratis.ExpenseTracker.DTO.Household;

import java.util.List;

public record HouseholdDetailedDTO(
    Long id,
    String name,
    Long creatorId,
    String creatorName,
    String creatorEmail,
    List<HouseholdMemberResponse> members
) {

}
