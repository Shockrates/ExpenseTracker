package com.sokratis.ExpenseTracker.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ApiResponse;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Service.HouseholdService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/household")
@Tag(name = "Household Controller", description = "APIs for managing household data")
public class HouseholdController {

    private final HouseholdService householdService;

    @GetMapping
    @Operation(summary = "Get all Households", description = "Fetch a list of all Households")
    public ResponseEntity<ApiResponse<List<HouseholdDTO>>> getAllHouseholds(){

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("List of Households", householdService.fetchAllHouseholds()));
    }

}
