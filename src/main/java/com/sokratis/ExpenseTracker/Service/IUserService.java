package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;

import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.Model.User;



public interface IUserService {
    
    /**
     * Fetches the list of all Expense entities.
     * @return a list of Expenses
     */
    List<UserDTO> fetchUserList();

     /**
     * Fetches an User entity.
     * @param UserId the ID of the User to fetch
     * @return the requested User
     */
    Optional<UserDTO> fetchUser(Long userId );

     /**
     * Saves a User entity.
     * @param User the User to save
     * @return the saved User
     */
    UserDTO saveUser(User user);

    /**
     * Updates an existing User entity.
     * @param User the User with updated information
     * @param UserId the ID of the User to update
     * @return the updated User
     */
    Optional<UserDTO> updateUser(Long UserId, User user);

    /**
     * Deletes a User entity by its ID.
     * @param UserId the ID of the User to delete
     */
    void deleteUserById(Long userId);
}
