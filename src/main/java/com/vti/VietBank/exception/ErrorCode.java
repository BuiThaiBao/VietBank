package com.vti.VietBank.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /* =========================================================
     * 1000 - 1099: Lỗi hệ thống (System)
     * ========================================================= */
    UNCATEGORIZED_EXCEPTION(1000, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Khóa thông điệp không hợp lệ", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 1100 - 1199: Xác thực/Ủy quyền (Auth)
     * ========================================================= */
    UNAUTHENTICATED(1100, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1101, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    AUTH_PHONE_REQUIRED(1102, "Số điện thoại là bắt buộc", HttpStatus.BAD_REQUEST),
    AUTH_PHONE_INVALID(1103, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    AUTH_PASSWORD_REQUIRED(1104, "Mật khẩu là bắt buộc", HttpStatus.BAD_REQUEST),
    AUTH_PASSWORD_WEAK(1105, "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 1200 - 1299: Người dùng (User)
     * ========================================================= */
    USER_EXISTED(1200, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1201, "Tên đăng nhập phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1202, "Mật khẩu phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1203, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    USER_INACTIVE(1204, "Người dùng không hoạt động", HttpStatus.BAD_REQUEST),
    USER_PHONE_REQUIRED(1205, "Số điện thoại là bắt buộc", HttpStatus.BAD_REQUEST),
    USER_PHONE_INVALID(1206, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_REQUIRED(1207, "Mật khẩu là bắt buộc", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_WEAK(1208, "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 1300 - 1399: Vai trò/Quyền (Role/Permission)
     * ========================================================= */
    ROLE_NOT_FOUND(1300, "Không tìm thấy vai trò", HttpStatus.BAD_REQUEST),
    PERMISSION_NAME_REQUIRED(1301, "Tên quyền là bắt buộc", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED(1302, "Tên vai trò là bắt buộc", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 1400 - 1499: Nhân viên (Staff)
     * ========================================================= */
    STAFF_USERID_REQUIRED(1400, "ID người dùng là bắt buộc", HttpStatus.BAD_REQUEST),
    STAFF_FULLNAME_REQUIRED(1401, "Họ và tên là bắt buộc", HttpStatus.BAD_REQUEST),
    STAFF_FULLNAME_INVALID(1402, "Họ và tên chỉ chứa chữ cái và khoảng trắng", HttpStatus.BAD_REQUEST),
    STAFF_PHONE_REQUIRED(1403, "Số điện thoại là bắt buộc", HttpStatus.BAD_REQUEST),
    STAFF_PHONE_INVALID(1404, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    STAFF_POSITION_REQUIRED(1405, "Chức vụ là bắt buộc", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 2000 - 2099: Khách hàng (Customer)
     * ========================================================= */
    CUSTOMER_NOT_FOUND(2000, "Không tìm thấy khách hàng", HttpStatus.NOT_FOUND),
    CUSTOMER_ALREADY_EXISTS(2001, "Khách hàng đã tồn tại", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_EXISTS(2002, "Số điện thoại đã được đăng ký", HttpStatus.BAD_REQUEST),
    CUSTOMER_CITIZENID_EXISTS(2003, "CCCD đã được đăng ký", HttpStatus.BAD_REQUEST),
    CUSTOMER_UPDATE_FAILED(2004, "Cập nhật khách hàng thất bại", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(2005, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_REQUIRED(2006, "Số điện thoại là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_INVALID(2007, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    CUSTOMER_PASSWORD_REQUIRED(2008, "Mật khẩu là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_PASSWORD_WEAK(2009, "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt", HttpStatus.BAD_REQUEST),
    CUSTOMER_FULLNAME_REQUIRED(2010, "Họ và tên là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_FULLNAME_INVALID(2011, "Họ và tên chỉ chứa chữ cái và khoảng trắng", HttpStatus.BAD_REQUEST),
    CUSTOMER_EMAIL_REQUIRED(2012, "Email là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_EMAIL_INVALID(2013, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    CUSTOMER_CITIZENID_REQUIRED(2014, "CCCD là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_CITIZENID_INVALID(2015, "CCCD phải có đúng 12 chữ số", HttpStatus.BAD_REQUEST),
    CUSTOMER_ADDRESS_REQUIRED(2016, "Địa chỉ là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_DOB_REQUIRED(2017, "Ngày sinh là bắt buộc", HttpStatus.BAD_REQUEST),
    CUSTOMER_DOB_INVALID(2018, "Khách hàng phải đủ 18 tuổi", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 3000 - 3099: Tài khoản (Account)
     * ========================================================= */
    ACCOUNT_NOT_FOUND(3000, "Không tìm thấy tài khoản", HttpStatus.NOT_FOUND),
    ACCOUNT_ALREADY_EXISTS(3001, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE(3002, "Tài khoản không hoạt động", HttpStatus.BAD_REQUEST),
    ACCOUNT_BALANCE_NOT_ENOUGH(3003, "Số dư không đủ", HttpStatus.BAD_REQUEST),
    ACCOUNT_UPDATE_FAILED(3004, "Cập nhật tài khoản thất bại", HttpStatus.BAD_REQUEST),
    ACCOUNT_CITIZENID_REQUIRED(3005, "CCCD là bắt buộc", HttpStatus.BAD_REQUEST),
    ACCOUNT_CITIZENID_INVALID(3006, "CCCD phải có đúng 12 chữ số", HttpStatus.BAD_REQUEST),
    ACCOUNT_BALANCE_REQUIRED(3007, "Số dư ban đầu là bắt buộc", HttpStatus.BAD_REQUEST),
    ACCOUNT_BALANCE_INVALID(3008, "Số dư phải lớn hơn hoặc bằng 0", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADD_MONEY_REQUIRED(3009, "Số tiền nạp là bắt buộc", HttpStatus.BAD_REQUEST),
    ACCOUNT_ADD_MONEY_INVALID(3010, "Số tiền nạp phải lớn hơn 0", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 4000 - 4099: Giao dịch (Transaction)
     * ========================================================= */
    TRANSACTION_NOT_FOUND(4000, "Không tìm thấy giao dịch", HttpStatus.NOT_FOUND),
    TRANSACTION_FAILED(4001, "Giao dịch thất bại", HttpStatus.BAD_REQUEST),
    TRANSACTION_INVALID_AMOUNT(4002, "Số tiền giao dịch phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    TRANSACTION_SAME_ACCOUNT(4003, "Không thể chuyển cùng tài khoản", HttpStatus.BAD_REQUEST),
    TRANSACTION_ROLLBACK(4004, "Giao dịch đã được hoàn tác", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_TRANSACTION(4005, "Giao dịch không được phép", HttpStatus.UNAUTHORIZED),
    TRANSACTION_FROM_ACCOUNT_REQUIRED(4006, "Tài khoản nguồn là bắt buộc", HttpStatus.BAD_REQUEST),
    TRANSACTION_TO_ACCOUNT_REQUIRED(4007, "Tài khoản đích là bắt buộc", HttpStatus.BAD_REQUEST),
    TRANSACTION_AMOUNT_REQUIRED(4008, "Số tiền giao dịch là bắt buộc", HttpStatus.BAD_REQUEST),
    TRANSACTION_AMOUNT_INVALID(4009, "Số tiền giao dịch phải lớn hơn 0", HttpStatus.BAD_REQUEST),
    TRANSACTION_ACCOUNT_NUMBER_INVALID(4010, "Số tài khoản phải có 10-14 chữ số", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 5000 - 5099: Xác thực dữ liệu (Validation)
     * ========================================================= */
    VALIDATION_ERROR(5000, "Xác thực dữ liệu thất bại", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(5001, "Thiếu trường bắt buộc", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT(5002, "Định dạng dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),

    /* =========================================================
     * 7000 - 7099: Khác/Compat (Khuyến nghị không dùng)
     * ========================================================= */

    SAME_ACCOUNT_TRANSFER(4003, "Không được chuyển cùng số tài khoản (Đã ngừng dùng, dùng TRANSACTION_SAME_ACCOUNT)", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
