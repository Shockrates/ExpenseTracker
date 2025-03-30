package com.sokratis.ExpenseTracker.Repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import com.sokratis.ExpenseTracker.Model.Token;


public interface TokenRepository extends JpaRepository<Token, Long>{

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserEmail(String email);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expirationTime < :now")
    void deleteExpiredTokens(Instant now);
    
}
