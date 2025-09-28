package com.vti.VietBank.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vti.VietBank.dto.request.auth.PermissionRequest;
import com.vti.VietBank.dto.response.auth.PermissionResponse;
import com.vti.VietBank.entity.Permission;
import com.vti.VietBank.mapper.PermissionMapper;
import com.vti.VietBank.repository.IPermissionRepository;
import com.vti.VietBank.service.IPermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
