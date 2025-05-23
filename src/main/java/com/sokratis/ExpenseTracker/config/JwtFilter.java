package com.sokratis.ExpenseTracker.config;


import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sokratis.ExpenseTracker.Service.JWTService;
import com.sokratis.ExpenseTracker.utils.ExceptionUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final ExceptionUtils utils;

    @Autowired
    public JwtFilter(UserDetailsService userDetailsService, JWTService jwtService, ExceptionUtils utils) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.utils = utils;
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            try {

                String requestPath = request.getRequestURI();
                if (requestPath.equals("/api/auth/login") || requestPath.equals("/api/auth/register")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                
                if (authHeader !=null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    username = jwtService.extractUsername(token);
                }
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                    
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    
                    if (jwtService.validateToken(token, userDetails ) && jwtService.isTokenStored(token)) {
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
    
                filterChain.doFilter(request, response);
                
            }catch (ExpiredJwtException e) {
                utils.handleJwtException(response, "JWT token has expired", "Token Expired", HttpStatus.UNAUTHORIZED);
                return;
            } catch (SignatureException e) {
                utils.handleJwtException(response, "Invalid token signature", "Invalid Signature", HttpStatus.UNAUTHORIZED);
                return;
            } catch (MalformedJwtException e) {
                utils.handleJwtException(response, "Malformed JWT token", "Malformed Token", HttpStatus.BAD_REQUEST);
                return;
            } catch (UnsupportedJwtException e) {
                utils.handleJwtException(response, "Unsupported JWT token", "Unsupported Token", HttpStatus.BAD_REQUEST);
                return;
            } catch (IllegalArgumentException e) {
                utils.handleJwtException(response, "JWT token is missing or empty", "Invalid Token", HttpStatus.BAD_REQUEST);
                return;
            }

            
    }

}
