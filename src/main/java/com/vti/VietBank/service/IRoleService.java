package com.vti.VietBank.service;

import com.vti.VietBank.dto.request.RoleRequest;
import com.vti.VietBank.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {

    public RoleResponse create(RoleRequest request);

    public List<RoleResponse> getAll();

    public void delete(String role);

}
