package com.vti.VietBank.dto.response.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountResponse {
    private String citizenId;
    private String fullName;
    private String accountNumber;
    private BigDecimal balance;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate updatedAt;
    private String isActive;

}
