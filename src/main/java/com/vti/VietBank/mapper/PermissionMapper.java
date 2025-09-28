package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vti.VietBank.dto.request.auth.PermissionRequest;
import com.vti.VietBank.dto.response.auth.PermissionResponse;
import com.vti.VietBank.entity.Permission;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    com.vti.VietBank.entity.Permission toPermission(PermissionRequest request);

    //    @Mapping(source = "lastName",target = "firstName")
    PermissionResponse toPermissionResponse(Permission permission);
    //    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
