package com.sokratis.ExpenseTracker.Service;

import java.util.List;
import java.util.Optional;


import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Model.Category;

public interface ICategoryService {


    /**
     * Fetches the list of all Category entities.
     * @return a list of Categories
     */
    List<Category> fetchCategoryList();

    /**
     * Fetches an Category entity.
     * @param CategoryId the ID of the Category to fetch
     * @return the requested Category
     */
    Optional<Category> fetchCategory(Long categoryId );

     /**
     * Saves a Category entity.
     * @param Category the Category to save
     * @return the saved Category
     */
    Category saveCategory(Category category);

    /**
     * Updates an existing Category entity.
     * @param updatedCategory the Category with updated information
     * @param CategoryId the ID of the Category to update
     * @return the updated Category
     */
    Optional<Category> updateCategory(Long categoryId, Category updatedCategory);

    /**
     * Deletes a Category entity by its ID.
     * @param CategoryId the ID of the Category to delete
     */
    void deleteCategoryById(Long categoryId);

    /**
     * Fetches the total of all Expense made by Use.
     * @param CategoryId the ID of the Category to calculate all expenses
     * @return the Total of all Expenses
     */
    public Double  calculateTotalbyCategory(Long CategoryId);

}
