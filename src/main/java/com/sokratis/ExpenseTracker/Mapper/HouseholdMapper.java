package com.sokratis.ExpenseTracker.Mapper;


import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.Model.Household;

public class HouseholdMapper {

    public static HouseholdDTO toDTO(Household h) {
        return new HouseholdDTO(
         
                h.getId(),
                h.getName(),
                h.getCreatedBy().getUserId(),
                h.getCreatedBy().getUserName()
        );
    }

    public static List<HouseholdDTO> toDTOList(List<Household> hList) {
        return hList.stream().map(h -> new HouseholdDTO(
                h.getId(),
                h.getName(),
                 h.getCreatedBy().getUserId(),
                h.getCreatedBy().getUserName()
        ))
        .toList();
    }
    
}
