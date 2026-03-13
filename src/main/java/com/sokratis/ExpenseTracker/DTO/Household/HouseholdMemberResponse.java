package com.sokratis.ExpenseTracker.DTO.Household;



public record HouseholdMemberResponse(
    Long memberId,
    String memberName,
    String memberEmail,
    String memberRole
) {}
