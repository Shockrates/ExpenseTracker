package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.DTO.LoginRequest;
import com.sokratis.ExpenseTracker.DTO.UserCreationRequest;
import com.sokratis.ExpenseTracker.Mapper.UserMapper;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Model.UserInfoDetails;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;
import com.sokratis.ExpenseTracker.Repository.TokenRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;
import com.sokratis.ExpenseTracker.Service.Interfaces.IUserService;
import com.sokratis.ExpenseTracker.utils.EntityUtils;
import com.sokratis.ExpenseTracker.utils.SecurityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final SecurityUtils utils;


    @Autowired
    AuthenticationManager authenticationManager;

    public String verifyUser(LoginRequest user) {

        //Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword())
        );

        //Check if the USer is Authenticated
        if (authentication.isAuthenticated()) {
            //Extract User Details from authentication
            UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
            //Extract Roles
            String roles = userDetails.getAuthoritiesAsString();
            //Generate the Token
            String jwtToken = jwtService.generateToken(user.getUserEmail(), roles);
            //Save token to the DB
            jwtService.saveToken(jwtToken);
            
            return jwtToken;
        }
 
        return "Failed to Login";
    }

    public void logout(String token) {
        jwtService.invalidateToken(token);
    }

    // Retrive all Users
    public List<UserDTO> fetchUserList(){  
        return UserMapper.toDTOList(userRepository.findAll());  
    }

    // Retrieve an User by ID
    public Optional<UserDTO> fetchUser(Long UserId ){
        return userRepository.findById(UserId)
        .map(UserMapper::toDTO);
    }

    // Create a new User
    public UserDTO saveUser(UserCreationRequest user) {
        
        if (user.getUserEmail() == null || user.getUserEmail() == "" ) {
            throw new IllegalArgumentException("Please Enter a valid email");
        }
        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        
        User createdUser = UserMapper.toEntity(user);
        createdUser.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return UserMapper.toDTO(userRepository.save(createdUser));
    }

    // Update user (except password)
    public Optional<UserDTO> updateUser(Long id, UserCreationRequest updatedUser) {


        return userRepository.findById(id).map(user -> {

            // Check if the new email already exists (excluding current user)
            Optional<User> existingUserWithEmail = userRepository.findByUserEmail(updatedUser.getUserEmail());
            if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getUserId().equals(id)) {
                throw new IllegalArgumentException("Email is already in use by another user!");
            }

            BeanUtils.copyProperties(updatedUser, user, EntityUtils.getNullPropertyNames(updatedUser));

            return UserMapper.toDTO(userRepository.save(user));
        });
    }

    // Update password (hashed)
    public void updatePassword(Long id, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setUserPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Delete user
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found!");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Double calculateTotalbyUser(Long UserId) {
        
        return expenseRepository.getTotalExpensesByUser(UserId);
    }

    
}
