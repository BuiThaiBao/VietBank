package com.vti.VietBank.dto.request.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vti.VietBank.dto.request.customer.validator.AgeConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    // SĐT bắt buộc, định dạng VN: 0xxxxxxxxx hoặc +84xxxxxxxxx
    @NotBlank(message = "CUSTOMER_PHONE_REQUIRED")
    @Pattern(
        regexp = "^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$",
        message = "CUSTOMER_PHONE_INVALID"
    )
    private String phoneNumber;
    
    // Mật khẩu bắt buộc, tối thiểu 8 ký tự, phải có chữ hoa, chữ thường, số và ký tự đặc biệt
    @NotBlank(message = "CUSTOMER_PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_INVALID")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "CUSTOMER_PASSWORD_WEAK"
    )
    private String password;
    
    // Họ và tên bắt buộc, chỉ chứa chữ cái tiếng Việt và khoảng trắng
    @NotBlank(message = "CUSTOMER_FULLNAME_REQUIRED")
    @Pattern(
        regexp = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$",
        message = "CUSTOMER_FULLNAME_INVALID"
    )
    private String fullName;
    
    // Email bắt buộc, đúng định dạng email
    @NotBlank(message = "CUSTOMER_EMAIL_REQUIRED")
    @Email(message = "CUSTOMER_EMAIL_INVALID")
    private String email;
    
    // CCCD bắt buộc, đúng 12 chữ số
    @NotBlank(message = "CUSTOMER_CITIZENID_REQUIRED")
    @Pattern(regexp = "^\\d{12}$", message = "CUSTOMER_CITIZENID_INVALID")
    private String citizenId;
    
    // Địa chỉ bắt buộc
    @NotBlank(message = "CUSTOMER_ADDRESS_REQUIRED")
    private String address;

    // Ngày sinh bắt buộc, phải đủ 18 tuổi
    @NotNull(message = "CUSTOMER_DOB_REQUIRED")
    @AgeConstraint(min = 18, message = "CUSTOMER_DOB_INVALID")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
}
