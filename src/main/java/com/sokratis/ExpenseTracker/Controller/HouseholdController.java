package com.sokratis.ExpenseTracker.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sokratis.ExpenseTracker.DTO.ApiResponse;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDetailedDTO;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Model.UserInfoDetails;
import com.sokratis.ExpenseTracker.Service.HouseholdService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/household")
@Tag(name = "Household Controller", description = "APIs for managing household data")
public class HouseholdController {

    private final HouseholdService householdService;

    @GetMapping
    @Operation(summary = "Get all Households", description = "Fetch a list of all Households")
    public ResponseEntity<ApiResponse<List<HouseholdDTO>>> getAllHouseholds() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("List of Households", householdService.fetchAllHouseholds()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Household", description = "Fetch a Household by id")
    public ResponseEntity<ApiResponse<HouseholdDTO>> getHouseholdById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Households Found", householdService.fetchHousehold(id)));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Get a Household with members", description = "Fetch a Household with members by id")
    public ResponseEntity<ApiResponse<HouseholdDetailedDTO>> getHouseholdWithMembersById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Households and Members Found",
                        householdService.fetchHouseholdWithMembers(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new household", description = "Add a new household to the system")
    public ResponseEntity<ApiResponse<HouseholdDTO>> createHousehold(
            @Valid @RequestBody HouseholdCreationRequest householdrequest,
            @AuthenticationPrincipal UserInfoDetails userDetails) {

        HouseholdDTO household = householdService.saveHousehold(householdrequest, userDetails.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Household Created", household));

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update household", description = "Update a  household details")
    public ResponseEntity<ApiResponse<HouseholdDTO>> updateHousehold(
            @PathVariable Long id,
            @Valid @RequestBody HouseholdCreationRequest householdrequest,
            @AuthenticationPrincipal UserInfoDetails userDetails) {

        HouseholdDTO household = householdService.updateHousehold(id, householdrequest, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Household Updated", household));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete household", description = "Delete a  household ")
    public ResponseEntity<ApiResponse<HouseholdDTO>> deleteHousehold(
            @PathVariable Long id,
            @AuthenticationPrincipal UserInfoDetails userDetails) {

        HouseholdDTO deletedHousehold = householdService.deleteHousehold(id, userDetails);

         return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Household Deleted", deletedHousehold));

    }


}
