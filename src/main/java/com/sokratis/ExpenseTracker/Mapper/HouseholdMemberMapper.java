package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;

import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdMemberResponse;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Model.HouseholdMember;

public class HouseholdMemberMapper {

    public static List<HouseholdMemberResponse> toDTOList(List<HouseholdMember> hList) {
        return hList.stream().map(h -> new HouseholdMemberResponse(
                h.getUser().getUserId(),
                h.getUser().getUserName(),
                h.getUser().getUserEmail(),
                h.getRole()))
                .toList();
    }

}
