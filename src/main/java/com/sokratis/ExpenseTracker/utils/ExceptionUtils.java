package com.sokratis.ExpenseTracker.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExceptionUtils {
    
    public void handleJwtException(HttpServletResponse response, String message, String error, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
    
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
    
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}
