package com.vti.VietBank.dto.request.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMoneyRequest {
    // Số tài khoản nguồn bắt buộc, 10-14 chữ số (chuẩn số TK ngân hàng VN)
    @NotBlank(message = "TRANSACTION_FROM_ACCOUNT_REQUIRED")
    @Pattern(regexp = "^\\d{10,14}$", message = "TRANSACTION_ACCOUNT_NUMBER_INVALID")
    private String fromAccount;
    
    // Số tài khoản đích bắt buộc, 10-14 chữ số
    @NotBlank(message = "TRANSACTION_TO_ACCOUNT_REQUIRED")
    @Pattern(regexp = "^\\d{10,14}$", message = "TRANSACTION_ACCOUNT_NUMBER_INVALID")
    private String toAccount;
    
    // Số tiền giao dịch bắt buộc, phải >= 1000 VND
    @NotNull(message = "TRANSACTION_AMOUNT_REQUIRED")
    @DecimalMin(value = "1000.0", inclusive = true, message = "TRANSACTION_AMOUNT_INVALID")
    private BigDecimal amount;
    
    // Nội dung chuyển khoản (không bắt buộc, giới hạn 500 ký tự)
    @Size(max = 500, message = "INVALID_FORMAT")
    private String description;
}
