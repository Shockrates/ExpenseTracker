package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.Mapper.UserMapper;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;
import com.sokratis.ExpenseTracker.Service.Interfaces.IUserService;
import com.sokratis.ExpenseTracker.utils.EntityUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    // @Autowired
    // private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public String verifyUser(User user) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUserEmail());
        }
 
        return "Failed to Login";
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
    public UserDTO saveUser(User user) {
        
        if (user.getUserEmail() == null || user.getUserEmail() == "" ) {
            throw new IllegalArgumentException("Please Enter a valid email");
        }
        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        
        // Hash the password
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return UserMapper.toDTO(userRepository.save(user));
    }

    // Update user (except password)
    public Optional<UserDTO> updateUser(Long id, UserDTO updatedUserDTO) {


        return userRepository.findById(id).map(user -> {

            // Check if the new email already exists (excluding current user)
            Optional<User> existingUserWithEmail = userRepository.findByUserEmail(updatedUserDTO.getUserEmail());
            if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getUserId().equals(id)) {
                throw new IllegalArgumentException("Email is already in use by another user!");
            }

            BeanUtils.copyProperties(updatedUserDTO, user, EntityUtils.getNullPropertyNames(updatedUserDTO));

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
