package com.vti.VietBank.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Integer id;
    private Integer fromAccountId; // tham chiếu đến Account
    private Integer toAccountId; // tham chiếu đến Account
    private BigDecimal amount;
    //    private String transactionType;
    private String description;
    private LocalDateTime createdAt;
}
