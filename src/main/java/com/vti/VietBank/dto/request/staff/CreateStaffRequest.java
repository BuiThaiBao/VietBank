package com.vti.VietBank.dto.request.staff;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStaffRequest {
    
    @NotNull(message = "STAFF_USERID_REQUIRED")
    private Long userId;
    
    // Họ và tên bắt buộc, chỉ chứa chữ cái tiếng Việt và khoảng trắng
    @NotBlank(message = "STAFF_FULLNAME_REQUIRED")
    @Pattern(
        regexp = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$",
        message = "STAFF_FULLNAME_INVALID"
    )
    private String fullName;
    
    // SĐT bắt buộc, định dạng VN: 0xxxxxxxxx hoặc +84xxxxxxxxx
    @NotBlank(message = "STAFF_PHONE_REQUIRED")
    @Pattern(
        regexp = "^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$",
        message = "STAFF_PHONE_INVALID"
    )
    private String phone;
    
    // Chức vụ bắt buộc 
    @NotBlank(message = "STAFF_POSITION_REQUIRED")
    private String position;
}
