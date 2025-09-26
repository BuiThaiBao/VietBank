package com.vti.VietBank.service;

import com.vti.VietBank.dto.request.PermissionRequest;
import com.vti.VietBank.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    public PermissionResponse create(PermissionRequest request);

    public List<PermissionResponse> getAll();

    public void delete(String permission);
}
