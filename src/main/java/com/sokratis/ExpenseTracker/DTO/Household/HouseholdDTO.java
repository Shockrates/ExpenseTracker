package com.sokratis.ExpenseTracker.DTO.Household;

public record HouseholdDTO(
    Long id,
    String name,
    Long creatorId,
    String creatorName ,
    Long membersCount
) {
    public HouseholdDTO(Long id, String name, Long creatorId, String creatorName) {
        this(id, name, creatorId, creatorName, null);
    }
    
}
