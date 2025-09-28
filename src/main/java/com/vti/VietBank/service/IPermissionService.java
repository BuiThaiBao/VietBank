package com.vti.VietBank.service;

import java.util.List;

import com.vti.VietBank.dto.request.auth.PermissionRequest;
import com.vti.VietBank.dto.response.auth.PermissionResponse;

public interface IPermissionService {
    public PermissionResponse create(PermissionRequest request);

    public List<PermissionResponse> getAll();

    public void delete(String permission);
}
