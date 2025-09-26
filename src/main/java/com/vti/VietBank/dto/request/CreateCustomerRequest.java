package com.vti.VietBank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String citizenId;
    private String address;
    private LocalDate dob;
}
