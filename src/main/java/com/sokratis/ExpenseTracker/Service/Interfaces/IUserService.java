package com.sokratis.ExpenseTracker.Service.Interfaces;

import java.util.List;
import java.util.Optional;

import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.DTO.UserCreationRequest;
import com.sokratis.ExpenseTracker.Model.User;



public interface IUserService {
    
    /**
     * Fetches the list of all User entities.
     * @return a list of Users
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
    UserDTO saveUser(UserCreationRequest user);

    /**
     * Updates an existing User entity.
     * @param updatedUserDTO the User with updated information
     * @param UserId the ID of the User to update
     * @return the updated User
     */
    Optional<UserDTO> updateUser(Long UserId, UserDTO updatedUserDTO);


    /**
     * Updates the password of an existing User entity.
     * @param ewPassword the User's updated password
     * @param UserId the ID of the User to update
     * @return the updated User
     */
    public void updatePassword(Long UserId, String newPassword);

    /**
     * Deletes a User entity by its ID.
     * @param UserId the ID of the User to delete
     */
    void deleteUserById(Long userId);

    /**
     * Fetches the total of all Expense made by Use.
     * @param UserId the ID of the User to calculate all expenses
     * @return the Total of all Expenses
     */
    public Double  calculateTotalbyUser(Long UserId);
}
