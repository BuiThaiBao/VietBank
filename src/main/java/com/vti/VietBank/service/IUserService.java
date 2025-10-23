package com.vti.VietBank.service;

import java.util.List;

import com.vti.VietBank.dto.request.user.UpdateUserRequest;
import com.vti.VietBank.dto.response.user.UserResponse;

public interface IUserService {

    public List<UserResponse> getAllUsers();

    public UserResponse getUserById(int id);

    //    public UserResponse getUserByUsername(String username);

    //    public UserResponse createUser(CreateUserRequest request);

    public UserResponse updateUser(int id, UpdateUserRequest request);
}
