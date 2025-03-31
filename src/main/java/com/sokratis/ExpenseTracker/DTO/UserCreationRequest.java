package com.sokratis.ExpenseTracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    
    private String userName;
    private String userEmail;
    private String userPassword;
}
