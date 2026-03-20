package com.sokratis.ExpenseTracker.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record UserDTO(
        Long userId,
        String userName,
        String userEmail,
        String userRoles

) {
}
