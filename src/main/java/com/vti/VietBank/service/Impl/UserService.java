package com.vti.VietBank.service.Impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vti.VietBank.dto.request.user.UpdateUserRequest;
import com.vti.VietBank.dto.response.user.UserResponse;
import com.vti.VietBank.entity.User;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.mapper.UserMapper;
import com.vti.VietBank.repository.IUserRepository;
import com.vti.VietBank.service.IUserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    IUserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {

        // viết thủ công
        //        return userRepository.findAll().stream().map(user -> new UserResponse(
        //                user.getId(),
        //                user.getUsername(),
        //                user.getRole().name(),
        //                user.getCreatedAt()
        //        )).toList();

        // dùng builder
        //        return userRepository.findAll()
        //                .stream()
        //                .map(user -> {
        //                    UserResponse response = new UserResponse();
        //                    response.setId(user.getId());
        //                    response.setUsername(user.getUsername());
        //                    response.setRole(user.getRole().name());
        //                    return response;
        //                })
        //                .toList();

        // dùng mapper
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(int id) {
        return userMapper.toResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS)));
    }

    //    @Override
    //    public UserResponse getUserByUsername(String username) {
    //        return userMapper.toResponse(
    //                userRepository.findByUsername(username).orElseThrow(() -> new
    // AppException(ErrorCode.USER_NOT_EXISTS)));
    //    }

    //    @Override
    //    public UserResponse createUser(CreateUserRequest request) {
    //        if (userRepository.existsByUsername(request.getUsername())) {
    //            throw new AppException(ErrorCode.USER_EXISTED);
    //        }
    //        User user = userMapper.toEntity(request);
    //        user.setPassword(passwordEncoder.encode(user.getPassword()));
    ////        user.setRole(Role.CUSTOMER);
    //        return userMapper.toResponse(userRepository.save(user));
    //    }

    @Override
    public UserResponse updateUser(int id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toResponse(userRepository.save(user));
    }
}
