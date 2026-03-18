package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDetailedDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdMemberResponse;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Model.HouseholdMember;

public class HouseholdMapper {

    public static HouseholdDTO toDTO(Household h) {
        return new HouseholdDTO(

                h.getId(),
                h.getName(),
                h.getCreatedBy().getUserId(),
                h.getCreatedBy().getUserName());
    }

    public static List<HouseholdDTO> toDTOList(List<Household> hList) {
        return hList.stream().map(h -> new HouseholdDTO(
                h.getId(),
                h.getName(),
                h.getCreatedBy().getUserId(),
                h.getCreatedBy().getUserName()))
                .toList();
    }

    public static HouseholdDetailedDTO toDetailedDTO(Household h, List<HouseholdMemberResponse> list) {
        return new HouseholdDetailedDTO(

                h.getId(),
                h.getName(),
                h.getCreatedBy().getUserId(),
                h.getCreatedBy().getUserName(),
                h.getCreatedBy().getUserEmail(),
                list.size(),
                list

        );
    }

    public static List<HouseholdDTO> toHouseholdDTOList(List<HouseholdMember> memberList) {
        return memberList.stream().map(member -> toDTO(member.getHousehold()))
                .toList();

    }

}
