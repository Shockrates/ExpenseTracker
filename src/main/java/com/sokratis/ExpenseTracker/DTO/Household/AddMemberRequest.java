package com.sokratis.ExpenseTracker.DTO.Household;

import com.sokratis.ExpenseTracker.Model.Enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(

                @NotNull(message = "Email cannot be null") @Email(message = "Invalid email format") String email,

                @NotNull(message = "Role must be provided") Role role) {

}
