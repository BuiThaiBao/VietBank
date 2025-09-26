package com.vti.VietBank.mapper;

import com.vti.VietBank.entity.Role;
import org.mapstruct.Mapper;

import com.vti.VietBank.dto.request.CreateUserRequest;
import com.vti.VietBank.dto.response.UserResponse;
import com.vti.VietBank.entity.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert Entity -> Response
    @Mapping(source = "role", target = "role")
    UserResponse toResponse(User user);
    default String map(Role role) {
        return role != null ? role.getName() : null; // hoặc role.toString()
    }

    User toEntity(CreateUserRequest request);
}
