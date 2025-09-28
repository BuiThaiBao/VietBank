package com.vti.VietBank.controller;

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

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    IAccountService accountService;

    @PostMapping("/create-account")
    ApiResponse<CreateAccountResponse> createCustomer(@RequestBody CreateAccountRequest request) {
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

    @PutMapping("update-account/{citizenId}")
    ApiResponse<UpdateAccountResponse> updateAccount(
            @PathVariable String citizenId, @RequestBody UpdateAccountRequest request) {
        return ApiResponse.<UpdateAccountResponse>builder()
                .success(true)
                .result(accountService.updateAccount(citizenId, request))
                .build();
    }

    @PutMapping("deactive-account/{accountNumber}")
    ApiResponse<String> deActiveAccount(@PathVariable String accountNumber) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(accountService.deActiveAccount(accountNumber))
                .build();
    }
}
