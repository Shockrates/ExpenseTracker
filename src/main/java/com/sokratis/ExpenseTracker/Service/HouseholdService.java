package com.sokratis.ExpenseTracker.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sokratis.ExpenseTracker.DTO.Household.AddMemberRequest;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdCreationRequest;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdDetailedDTO;
import com.sokratis.ExpenseTracker.DTO.Household.HouseholdMemberResponse;
import com.sokratis.ExpenseTracker.Exceptions.ResourceNotFoundException;
import com.sokratis.ExpenseTracker.Mapper.ExpenseMapper;
import com.sokratis.ExpenseTracker.Mapper.HouseholdMapper;
import com.sokratis.ExpenseTracker.Mapper.HouseholdMemberMapper;
import com.sokratis.ExpenseTracker.Model.Expense;
import com.sokratis.ExpenseTracker.Model.Household;
import com.sokratis.ExpenseTracker.Model.HouseholdMember;
import com.sokratis.ExpenseTracker.Model.User;
import com.sokratis.ExpenseTracker.Model.UserInfoDetails;
import com.sokratis.ExpenseTracker.Model.Enums.Role;
import com.sokratis.ExpenseTracker.Repository.HouseholdMemberRepository;
import com.sokratis.ExpenseTracker.Repository.HouseholdRepository;
import com.sokratis.ExpenseTracker.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserRepository userRepository;
    private final HouseholdMemberRepository memberRepository;

    public List<HouseholdDTO> fetchAllHouseholds() {
        return HouseholdMapper.toDTOList(householdRepository.findAllWithCreatedBy());
    }

    public HouseholdDTO fetchHousehold(Long id) {

        return HouseholdMapper.toDTO(
                householdRepository.findWithCreatedById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Household not found")));

    }

    @Transactional
    public HouseholdDetailedDTO fetchHouseholdWithMembers(Long id) {

        Household household = householdRepository.findWithCreatedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        List<HouseholdMemberResponse> members = HouseholdMemberMapper.toDTOList(memberRepository.findByHouseholdId(id));

        return HouseholdMapper.toDetailedDTO(household, members);

    }

    @Transactional
    public HouseholdDTO saveHousehold(HouseholdCreationRequest householdRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Household household = new Household();
        household.setName(householdRequest.name());
        household.setCreatedBy(user);

        HouseholdMember member = HouseholdMember.builder()
                .household(household)
                .user(user)
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);

        return HouseholdMapper.toDTO(householdRepository.save(household));
    }

    @Transactional
    public HouseholdDTO updateHousehold(Long householdId, HouseholdCreationRequest householdRequest,
            UserInfoDetails user) {

        Household household = householdRepository.findWithCreatedById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = household.getCreatedBy().getUserId().equals(user.getId());

        if (!isCreator && !isAdmin) {
            throw new AccessDeniedException("You dont have perimision for this update");
        }

        household.setName(householdRequest.name());
        return HouseholdMapper.toDTO(householdRepository.save(household));

    }

    @Transactional
    public HouseholdDTO deleteHousehold(Long HouseholdId, UserInfoDetails user) {

        Household household = householdRepository.findWithCreatedById(HouseholdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isCreator = household.getCreatedBy().getUserId().equals(user.getId());

        if (!isCreator && !isAdmin) {
            throw new AccessDeniedException("You dont have perimision for this deletion");
        }
        householdRepository.deleteById(HouseholdId);
        return HouseholdMapper.toDTO(household);

    }

    @Transactional
    public HouseholdMemberResponse addMemeber(Long householdId, AddMemberRequest memberRequest) {

        // boolean useExists = userRepository.existsByUserEmail(email);
        User user = userRepository.findByUserEmail(memberRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Household household = householdRepository.findWithCreatedById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

        boolean relationExists = memberRepository.existsByHouseholdIdAndUserUserId(householdId, user.getUserId());

        // TO DO: Validation if user is already member.
        // Validation if logged use can add members

        HouseholdMember addedMember = HouseholdMember.builder()
                .household(household)
                .user(user)
                .role(memberRequest.role())
                .build();
        memberRepository.save(addedMember);

        return HouseholdMemberMapper.toDTO(addedMember);

    }

}
