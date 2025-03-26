package com.sokratis.ExpenseTracker.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.UserInfoDetails;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;

@Component
public class SecurityUtils {

    @Autowired
    private ExpenseRepository expenseRepository;
    
    public Long getPrincipalId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserInfoDetails userDetails) {
            return userDetails.getId();
        }
        throw new AccessDeniedException("Unauthorized access");
    }
    
    public boolean isOwnerOfExpense(Long expenseId) {
        Long authenticatedId = getPrincipalId();
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        return expense.getExpenseUser().getUserId().equals(authenticatedId);
    }
}
