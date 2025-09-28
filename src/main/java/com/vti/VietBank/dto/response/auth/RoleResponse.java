package com.vti.VietBank.dto.response.auth;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    @NotBlank
    String name;

    String description;
    Set<PermissionResponse> permissions;
}
