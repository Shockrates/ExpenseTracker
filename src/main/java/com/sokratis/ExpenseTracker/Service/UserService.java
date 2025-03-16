package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import com.sokratis.ExpenseTracker.DTO.UserDTO;

import com.sokratis.ExpenseTracker.Mapper.UserMapper;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Repository.ExpenseRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;

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

            // ✅ Check if the new email already exists (excluding current user)
            Optional<User> existingUserWithEmail = userRepository.findByUserEmail(updatedUserDTO.getUserEmail());
            if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getUserId().equals(id)) {
                throw new IllegalArgumentException("Email is already in use by another user!");
            }

            user.setUserName(updatedUserDTO.getUserName());
            user.setUserEmail(updatedUserDTO.getUserEmail());
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
