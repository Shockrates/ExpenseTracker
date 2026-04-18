package com.sokratis.ExpenseTracker.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.Category.CategoryCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryDetailedDTO;
import com.sokratis.ExpenseTracker.DTO.Category.CategoryTotalDTO;
import com.sokratis.ExpenseTracker.DTO.Common.ApiResponse;
import com.sokratis.ExpenseTracker.Model.Category;
import com.sokratis.ExpenseTracker.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

        private final CategoryService categoryService;

        @Transactional
        @GetMapping
        @Operation(summary = "Get all Categories", description = "Fetch a list of all Categories")
        public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {

                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success("List of Categories", categoryService.fetchCategoryList()));
        }

        @GetMapping("/household/{householdId}")
        @Operation(summary = "Get Categories by Household ID", description = "Fetch a list of Categories for a specific Household")
        public ResponseEntity<ApiResponse<List<CategoryTotalDTO>>> getCategoriesByHouseholdId(
                        @PathVariable Long householdId,
                        @RequestParam(required = false) LocalDate startDate,
                        @RequestParam(required = false) LocalDate endDate) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success("Categories for Household",
                                                categoryService.fetchCategoryListWithTotals(householdId, startDate,
                                                                endDate)));
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get Categroy by ID", description = "Fetch a single Category by its ID")
        public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(@PathVariable Long id) {

                return categoryService.fetchCategory(id)
                                .map(category -> ResponseEntity.status(HttpStatus.OK)
                                                .body(ApiResponse.success("Category Found", category)))
                                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                .body(ApiResponse.error("Category not found with id " + id)));
        }

        @PostMapping
        @Operation(summary = "Register a Category", description = "Add a new Category to the system")
        public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryCreationRequest category) {

                CategoryDTO createdCategory = categoryService.saveCategory(category);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Category Created", createdCategory));

        }

        @PutMapping("/{id}")
        @Operation(summary = "Update existing Category", description = "Update a Category's details")
        public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id,
                        @RequestBody CategoryCreationRequest category) {

                CategoryDTO updatedCategory = categoryService.updateCategory(id, category);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success("Category Updated", updatedCategory));

        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete a Category", description = "Remove a Category from the system")
        public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
                try {
                        categoryService.deleteCategoryById(id);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                } catch (RuntimeException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(ApiResponse.error("Category not found with id " + id));
                }
        }

        @GetMapping("/{id}/total")
        @Operation(summary = "Get the total of a Category", description = "Get the total ammount of all expenses of a single Category")
        public ResponseEntity<ApiResponse<Double>> getCategoryTotalExpenseAmount(@PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success("Total amount of category is",
                                                categoryService.calculateTotalbyCategory(id)));
        }

        @GetMapping("/{id}/expenses")
        @Operation(summary = "Get Categroy and Expenses by Category ID", description = "Fetch a single Category and all of its related expenses by its ID")
        public ResponseEntity<ApiResponse<CategoryDetailedDTO>> getCategoryWithExpenses(@PathVariable Long id) {
                System.out.println("All Categories Expenses");
                return ResponseEntity.status(HttpStatus.OK).body(
                                ApiResponse.success("Total amount of category is",
                                                categoryService.fetchCategoryWithExpenses(id)));
        }

}
