package com.vti.VietBank.dto.request.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequest {
    // CCCD bắt buộc, đúng 12 chữ số (chuẩn CCCD mới của Việt Nam)
    @NotBlank(message = "ACCOUNT_CITIZENID_REQUIRED")
    @Pattern(regexp = "^\\d{12}$", message = "ACCOUNT_CITIZENID_INVALID")
    private String citizenId;
    
    // Số dư ban đầu bắt buộc, phải >= 0 (cho phép mở tài khoản với số dư 0)
    @NotNull(message = "ACCOUNT_BALANCE_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true, message = "ACCOUNT_BALANCE_INVALID")
    private BigDecimal balance;
}
