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
                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                "Household not found")));

        }

        @Transactional
        public HouseholdDetailedDTO fetchHouseholdWithMembers(Long id) {

                Household household = householdRepository.findWithCreatedById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

                List<HouseholdMemberResponse> members = HouseholdMemberMapper
                                .toDTOList(memberRepository.findByHouseholdId(id));

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
        public HouseholdMemberResponse addMemeber(Long householdId, AddMemberRequest memberRequest,
                        UserInfoDetails user) {

                // Retrive New Member from DB
                User newMember = userRepository.findByUserEmail(memberRequest.email())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // Retrieve household from DB
                Household household = householdRepository.findWithCreatedById(householdId)
                                .orElseThrow(() -> new IllegalArgumentException("Household not found"));

                boolean relationExists = memberRepository.existsByHouseholdIdAndUserUserId(householdId,
                                newMember.getUserId());
                if (relationExists) {
                        throw new IllegalArgumentException("User already member of this household");
                }

                boolean isHouseholeAdmin = memberRepository.existsByHouseholdIdAndUserUserIdAndRole(householdId,
                                user.getId(), Role.ADMIN);
                if (!isHouseholeAdmin) {
                        throw new AccessDeniedException("Only household admins can add members");
                }

                HouseholdMember addedMember = HouseholdMember.builder()
                                .household(household)
                                .user(newMember)
                                .role(memberRequest.role())
                                .build();
                memberRepository.save(addedMember);

                return HouseholdMemberMapper.toDTO(addedMember);

        }

        @Transactional
        public void deleteMemeber(Long householdId, Long memberId,
                        UserInfoDetails user) {

                HouseholdMember requestUser = memberRepository.findByHouseholdIdAndUserUserId(householdId,
                                user.getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Only household members can perform this action"));

                if (requestUser.getRole() != Role.ADMIN) {
                        throw new AccessDeniedException("Only household admins can add members");
                }

                HouseholdMember targetMember = memberRepository.findByHouseholdIdAndUserUserId(householdId,
                                memberId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "This User is not a member"));

                if (targetMember.getRole() == Role.ADMIN) {
                        throw new AccessDeniedException("You cannot delete another Admin");
                }

                memberRepository.delete(targetMember);

        }

}
