package com.vti.VietBank.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {
    private Integer id;
    private Integer userId; // tham chiếu đến User
    private String fullName;
    private String phone;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
