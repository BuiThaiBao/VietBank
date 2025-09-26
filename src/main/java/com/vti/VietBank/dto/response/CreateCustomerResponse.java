package com.vti.VietBank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerResponse {
    private String fullName;
    private String phone;
    private String email;
    private String citizenId;
    private String address;
    private LocalDate dob;

}
