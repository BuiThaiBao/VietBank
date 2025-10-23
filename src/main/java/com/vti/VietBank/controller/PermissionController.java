package com.vti.VietBank.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vti.VietBank.dto.request.auth.PermissionRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.auth.PermissionResponse;
import com.vti.VietBank.service.IPermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Validated
@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @PostMapping()
    ApiResponse<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        PermissionResponse permission = permissionService.create(request);
        return ApiResponse.<PermissionResponse>builder()
                .success(true)
                .result(permission)
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .success(true)
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        return ApiResponse.<String>builder()
                .success(true)
                .result("Permission has been deleted")
                .build();
    }
}
