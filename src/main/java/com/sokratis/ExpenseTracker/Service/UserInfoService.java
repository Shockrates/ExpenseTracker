package com.sokratis.ExpenseTracker.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Model.UserInfoDetails;
import com.sokratis.ExpenseTracker.Repository.UserRepository;

@Service
public class UserInfoService implements UserDetailsService{

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUserEmail(username); // Assuming 'email' is used as username
        
        // Converting User to UserDetails
        return user.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
}
