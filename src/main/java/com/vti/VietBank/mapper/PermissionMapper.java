package com.vti.VietBank.mapper;


import com.vti.VietBank.dto.request.PermissionRequest;
import com.vti.VietBank.dto.response.PermissionResponse;
import com.vti.VietBank.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    com.vti.VietBank.entity.Permission toPermission(PermissionRequest request);

    //    @Mapping(source = "lastName",target = "firstName")
    PermissionResponse toPermissionResponse(Permission permission);
    //    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
