package com.sokratis.ExpenseTracker.DTO.User;

public record UserDTO(
        Long userId,
        String userName,
        String userEmail,
        String userRoles

) {
}
