package com.vti.VietBank.dto.request.customer;

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
public class CreateCustomerRequest {
    private String phoneNumber;
    private String password;
    private String fullName;
    private String email;
    private String citizenId;
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
}
