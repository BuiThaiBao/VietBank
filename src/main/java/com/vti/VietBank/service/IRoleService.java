package com.vti.VietBank.service;

import java.util.List;

import com.vti.VietBank.dto.request.auth.RoleRequest;
import com.vti.VietBank.dto.response.auth.RoleResponse;

public interface IRoleService {

    public RoleResponse create(RoleRequest request);

    public List<RoleResponse> getAll();

    public void delete(String role);
}
