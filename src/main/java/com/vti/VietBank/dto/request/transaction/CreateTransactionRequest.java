package com.vti.VietBank.dto.request.transaction;

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
public class CreateTransactionRequest {
    // Tài khoản nguồn bắt buộc (tài khoản chuyển tiền)
    @NotNull(message = "TRANSACTION_FROM_ACCOUNT_REQUIRED")
    private Long fromAccountId;
    
    // Tài khoản đích bắt buộc (tài khoản nhận tiền)
    @NotNull(message = "TRANSACTION_TO_ACCOUNT_REQUIRED")
    private Long toAccountId;
    
    // Số tiền giao dịch bắt buộc, phải > 0 (tối thiểu 1000 VND cho thực tế)
    @NotNull(message = "TRANSACTION_AMOUNT_REQUIRED")
    @DecimalMin(value = "1000.0", inclusive = true, message = "TRANSACTION_AMOUNT_INVALID")
    private BigDecimal amount;
    
    // Nội dung chuyển khoản (không bắt buộc, nhưng khuyến khích)
    private String description;
}
