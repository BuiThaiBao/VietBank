package com.vti.VietBank.dto.response.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMoneyResponse {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}
