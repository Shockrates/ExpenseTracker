package com.sokratis.ExpenseTracker.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.sokratis.ExpenseTracker.DTO.UserDTO;
import com.sokratis.ExpenseTracker.DTO.UserCreationRequest;
import com.sokratis.ExpenseTracker.Model.User;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        } 
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return dto;
    }
    
    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = modelMapper.map(dto, User.class);
        return user;
    }

    public static User toEntity(UserCreationRequest request) {
        if (request == null) {
            return null;
        }
        User user = modelMapper.map(request, User.class);
        return user;
    }

    
    public static List<User> toEntityList(List<UserDTO> dtoList) {
        return dtoList.stream()
            .map(dto -> toEntity(dto))
            .collect(Collectors.toList());
    }
}
