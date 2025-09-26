package com.vti.VietBank.mapper;


import com.vti.VietBank.dto.request.RoleRequest;
import com.vti.VietBank.dto.response.RoleResponse;
import com.vti.VietBank.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
