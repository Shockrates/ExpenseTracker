package com.sokratis.ExpenseTracker.Model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "valid_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(unique = true, nullable = false, length = 500)
    private String token;

    @Column(name = "user_email",unique = true, nullable = false)
    private String userEmail;


    @Column(nullable = false)
    private Instant expirationTime;

    public Token(String token, Instant expirationTime, String userEmail) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.userEmail = userEmail;
    }

    
}
