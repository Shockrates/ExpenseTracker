package com.sokratis.ExpenseTracker.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.sokratis.ExpenseTracker.Exceptions.ResourceNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {


   

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errorMessage.append("- ").append(error.getDefaultMessage()).append("\n")
        );
        return buildResponse(errorMessage.toString(),"Invalid values", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex){ 
        System.out.println("Logging");
        return buildResponse(ex.getMessage(),"Resource Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex){ 
        System.out.println("Logging");
        return buildResponse(ex.getMessage(),"Illegal Argument", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "You do not have permission to perform this action.";
        return buildResponse(  message,"Access Denied", HttpStatus.FORBIDDEN);
    }


    private ResponseEntity<Map<String, Object>> buildResponse(String message, String error, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
