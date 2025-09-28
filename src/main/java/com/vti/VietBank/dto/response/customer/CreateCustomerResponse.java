package com.vti.VietBank.dto.response.customer;

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
public class CreateCustomerResponse {
    private String fullName;
    private String citizenId;
    private String phoneNumber;
    private String email;
    private String address;
    private String isActive;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;
}
