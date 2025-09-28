package com.vti.VietBank.dto.request.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCustomerRequest {
    String fullName;
    String email;
    String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
}
