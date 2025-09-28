package com.vti.VietBank.dto.response;

import java.time.LocalDateTime;

import com.vti.VietBank.dto.response.auth.RoleResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String phoneNumber;
    private RoleResponse role;
    private LocalDateTime createdAt;
}
