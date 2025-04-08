package com.sokratis.ExpenseTracker.Service;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.Exceptions.ResourceNotFoundException;
import com.sokratis.ExpenseTracker.Model.Token;
import com.sokratis.ExpenseTracker.Repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JWTService {
    
    private String secretkey = "";

    @Autowired
    private TokenRepository tokenRepository;

    public JWTService() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

     public String generateToken(String email, String authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities);
        
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .and()
                .signWith(getSignKey())
                .compact();
    }

    public void saveToken(String token) {
        String email = extractUsername(token);
        // Find existing token entry
        Optional<Token> existingToken = tokenRepository.findByUserEmail(email);
        if (existingToken.isPresent()) {
            // Update the existing token instead of creating a new one
            Token tokenEntity = existingToken.get();
            tokenEntity.setToken(token);
            tokenEntity.setExpirationTime(extractExpiration(token).toInstant());
            tokenRepository.save(tokenEntity);
        } else {
            // Save new token if no entry exists
            tokenRepository.save(new Token(token, extractExpiration(token).toInstant(), email));
        }
        
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if a token exists before validation
    public boolean isTokenStored(String token) {
        return tokenRepository.findByToken(token).isPresent();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public void invalidateToken(String token) {
        //tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
        tokenRepository.findByToken(token).ifPresentOrElse(
            tokenRepository::delete , 
            () -> { throw new ResourceNotFoundException("Token not found!"); }
        );
    }

     // Cleanup: Runs every Day to delete expired tokens
    @Scheduled(fixedRate = 60 * 60 * 24 * 1000) // Every Day
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(Instant.now());
    }



}
