package com.vti.VietBank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vti.VietBank.dto.request.auth.RoleRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.auth.RoleResponse;
import com.vti.VietBank.service.IRoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    IRoleService roleService;

    @PostMapping()
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        RoleResponse role = roleService.create(request);
        return ApiResponse.<RoleResponse>builder().success(true).result(role).build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .success(true)
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<String> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<String>builder()
                .success(true)
                .result("Role has been deleted")
                .build();
    }
}
