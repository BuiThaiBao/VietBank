package com.vti.VietBank.service.Impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vti.VietBank.dto.request.auth.RoleRequest;
import com.vti.VietBank.dto.response.auth.RoleResponse;
import com.vti.VietBank.mapper.RoleMapper;
import com.vti.VietBank.repository.IPermissionRepository;
import com.vti.VietBank.repository.IRoleRepository;
import com.vti.VietBank.service.IRoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    IRoleRepository roleRepository;
    RoleMapper roleMapper;
    IPermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
