package com.sokratis.ExpenseTracker.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sokratis.ExpenseTracker.Model.UserInfoDetails;

@Component
public class SecurityUtils {
    
    public Long getPrincipalId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserInfoDetails userDetails) {
            return userDetails.getId();
        }
        throw new AccessDeniedException("Unauthorized access");
    }
}
