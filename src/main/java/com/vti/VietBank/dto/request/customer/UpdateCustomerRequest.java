package com.vti.VietBank.dto.request.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vti.VietBank.dto.request.customer.validator.AgeConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCustomerRequest {
    // Họ và tên không bắt buộc, nhưng nếu có phải hợp lệ (chỉ chữ cái tiếng Việt và khoảng trắng)
    @Pattern(
        regexp = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$",
        message = "CUSTOMER_FULLNAME_INVALID"
    )
    String fullName;
    
    // Email không bắt buộc, nhưng nếu có phải đúng định dạng
    @Email(message = "CUSTOMER_EMAIL_INVALID")
    String email;
    
    // Địa chỉ không bắt buộc (có thể update hoặc không)
    String address;

    // Ngày sinh không bắt buộc, nhưng nếu có phải đủ 18 tuổi
    @AgeConstraint(min = 18, message = "CUSTOMER_DOB_INVALID")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
}
