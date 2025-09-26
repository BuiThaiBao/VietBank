package com.vti.VietBank.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // System
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),

    // User / Auth
    USER_EXISTED(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1005, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),


    // Customer
    CUSTOMER_NOT_FOUND(2001, "Customer not found", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(2007, "Email already exists", HttpStatus.BAD_REQUEST),
    CUSTOMER_ALREADY_EXISTS(2002, "Customer already exists", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_EXISTS(2003, "Phone number already registered", HttpStatus.BAD_REQUEST),
    CUSTOMER_CITIZENID_EXISTS(2004, "Citizen ID already registered", HttpStatus.BAD_REQUEST),
    CUSTOMER_UPDATE_FAILED(2005, "Failed to update customer info", HttpStatus.BAD_REQUEST),

    // Account
    ACCOUNT_NOT_FOUND(3001, "Account not found", HttpStatus.NOT_FOUND),
    ACCOUNT_ALREADY_EXISTS(3002, "Account already exists", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE(3003, "Account is inactive", HttpStatus.BAD_REQUEST),
    ACCOUNT_BALANCE_NOT_ENOUGH(3004, "Insufficient balance", HttpStatus.BAD_REQUEST),
    ACCOUNT_UPDATE_FAILED(3005, "Failed to update account", HttpStatus.BAD_REQUEST),

    // Transaction
    TRANSACTION_NOT_FOUND(4001, "Transaction not found", HttpStatus.NOT_FOUND),
    TRANSACTION_FAILED(4002, "Transaction failed", HttpStatus.BAD_REQUEST),
    TRANSACTION_INVALID_AMOUNT(4003, "Transaction amount must be greater than zero", HttpStatus.BAD_REQUEST),
    TRANSACTION_SAME_ACCOUNT(4004, "Cannot transfer to the same account", HttpStatus.BAD_REQUEST),
    TRANSACTION_ROLLBACK(4005, "Transaction rolled back", HttpStatus.INTERNAL_SERVER_ERROR),

    // Validation
    VALIDATION_ERROR(5001, "Validation failed", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(5002, "Missing required field", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT(5003, "Invalid data format", HttpStatus.BAD_REQUEST),

    // Role
    ROLE_NOT_FOUND(6001,"Role not found",HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
