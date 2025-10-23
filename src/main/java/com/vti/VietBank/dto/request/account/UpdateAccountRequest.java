package com.vti.VietBank.dto.request.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {
    // Số tiền nạp bắt buộc, phải > 0 (không cho phép nạp 0 hoặc âm)
    @NotNull(message = "ACCOUNT_ADD_MONEY_REQUIRED")
    @DecimalMin(value = "0.01", inclusive = true, message = "ACCOUNT_ADD_MONEY_INVALID")
    private BigDecimal addMoney;
}
