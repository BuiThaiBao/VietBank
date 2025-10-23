package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vti.VietBank.dto.request.user.CreateUserRequest;
import com.vti.VietBank.dto.response.user.UserResponse;
import com.vti.VietBank.entity.Role;
import com.vti.VietBank.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    // Convert Entity -> Response
    @Mapping(source = "role", target = "role")
    UserResponse toResponse(User user);

    default String map(Role role) {
        return role != null ? role.getName() : null; // hoặc role.toString()
    }

    User toEntity(CreateUserRequest request);
}
