package com.sokratis.ExpenseTracker.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.Mapper.HouseholdMapper;
import com.sokratis.ExpenseTracker.Repository.HouseholdRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepsoitory;

    public List<HouseholdDTO> fetchAllHouseholds(){
        return HouseholdMapper.toDTOList(householdRepsoitory.findAllWithCreatedBy()) ;
    }
    
}
