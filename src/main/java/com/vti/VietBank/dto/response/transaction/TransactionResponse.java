package com.vti.VietBank.dto.response.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
