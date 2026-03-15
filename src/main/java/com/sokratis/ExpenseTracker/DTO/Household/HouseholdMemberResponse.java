package com.sokratis.ExpenseTracker.DTO.Household;

import com.sokratis.ExpenseTracker.Model.Enums.Role;

public record HouseholdMemberResponse(
        Long memberId,
        String memberName,
        String memberEmail,
        Role memberRole) {
}
