package com.vti.VietBank.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vti.VietBank.dto.request.account.CreateAccountRequest;
import com.vti.VietBank.dto.request.account.UpdateAccountRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.account.CreateAccountResponse;
import com.vti.VietBank.dto.response.account.CustomerAccountResponse;
import com.vti.VietBank.dto.response.account.UpdateAccountResponse;
import com.vti.VietBank.service.IAccountService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Validated
@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    IAccountService accountService;

    @PostMapping("/create-account")
    ApiResponse<CreateAccountResponse> createCustomer(@Valid  @RequestBody CreateAccountRequest request) {
        CreateAccountResponse account = accountService.createAccount(request);
        return ApiResponse.<CreateAccountResponse>builder()
                .success(true)
                .result(account)
                .build();
    }

    @GetMapping("/get-my-account")
    ApiResponse<CustomerAccountResponse> getMyAccount() {
        return ApiResponse.<CustomerAccountResponse>builder()
                .success(true)
                .result(accountService.getMyAccount())
                .build();
    }

    @PutMapping("update-account/{accountNumber}")
    ApiResponse<UpdateAccountResponse> updateAccount(
            @PathVariable String accountNumber,@Valid @RequestBody UpdateAccountRequest request) {
        return ApiResponse.<UpdateAccountResponse>builder()
                .success(true)
                .result(accountService.updateAccount(accountNumber, request))
                .build();
    }

    @PutMapping("deactive-account/{accountNumber}")
    ApiResponse<String> deActiveAccount(@PathVariable
                                        @Pattern(regexp = "^\\d{10,14}$", message = "TRANSACTION_ACCOUNT_NUMBER_INVALID")
                                        String accountNumber) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(accountService.deActiveAccount(accountNumber))
                .build();
    }
}
