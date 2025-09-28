package com.vti.VietBank.dto.request.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMoneyRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String description;
}
