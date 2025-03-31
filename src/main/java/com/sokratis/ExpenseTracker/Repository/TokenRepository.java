package com.sokratis.ExpenseTracker.Repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sokratis.ExpenseTracker.Model.Token;

import jakarta.transaction.Transactional;


public interface TokenRepository extends JpaRepository<Token, Long>{

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token t WHERE t.expirationTime < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
    
}
