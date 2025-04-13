package com.sokratis.ExpenseTracker.DTO;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sokratis.ExpenseTracker.Model.HttpResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    public ResponseEntity<HttpResponse> success(T data, String message, HttpStatus status){
        return ResponseEntity.status(status).body(
            HttpResponse.builder()
            .timeStamp(Instant.now().toString())
            .data(Map.of("data", data))
            .message(message)
            .status(status)
            .statusCode(status.value())
            .build()
        );
    }
}
