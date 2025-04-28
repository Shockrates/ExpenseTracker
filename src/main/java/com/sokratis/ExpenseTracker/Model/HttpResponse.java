package com.sokratis.ExpenseTracker.Model;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;




@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse {

    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected Map<?,?> data;

    public HttpResponse(Map<?,?> data, String message, HttpStatus status){
        this.timeStamp = Instant.now().toString();
        this.message = message;
        this.data = data;
        this.status=status;
        this.statusCode = status.value();
    }

    public ResponseEntity<HttpResponse> success(){
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


