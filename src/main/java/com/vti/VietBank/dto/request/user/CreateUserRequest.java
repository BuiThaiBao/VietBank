package com.vti.VietBank.dto.request.user;

import jakarta.validation.constraints.NotBlank;
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
public class CreateUserRequest {
    // SĐT bắt buộc, định dạng VN: 0xxxxxxxxx hoặc +84xxxxxxxxx
    @NotBlank(message = "USER_PHONE_REQUIRED")
    @Pattern(
        regexp = "^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$",
        message = "USER_PHONE_INVALID"
    )
    private String phoneNumber;
    
    // Mật khẩu bắt buộc, tối thiểu 8 ký tự, phải có chữ hoa, chữ thường, số và ký tự đặc biệt
    @NotBlank(message = "USER_PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_INVALID")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "USER_PASSWORD_WEAK"
    )
    private String password;
}
