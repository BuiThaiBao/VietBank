package com.vti.VietBank.dto.request.user;

import com.vti.VietBank.enums.Role;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    // Mật khẩu không bắt buộc, nhưng nếu update phải >= 8 ký tự và có độ phức tạp
    @Size(min = 8, message = "PASSWORD_INVALID")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "USER_PASSWORD_WEAK"
    )
    private String password;
    
    // Role không bắt buộc (có thể update hoặc không)
    private Role role;
}
