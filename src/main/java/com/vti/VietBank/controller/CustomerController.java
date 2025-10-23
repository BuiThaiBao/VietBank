package com.vti.VietBank.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vti.VietBank.dto.request.customer.CreateCustomerRequest;
import com.vti.VietBank.dto.request.customer.UpdateCustomerRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.customer.CreateCustomerResponse;
import com.vti.VietBank.dto.response.customer.CustomerResponse;
import com.vti.VietBank.dto.response.customer.UpdateCustomerResponse;
import com.vti.VietBank.service.ICustomerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Validated
@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {
    ICustomerService customerService;

    //    @PostMapping("/create-user")
    //    ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest request) {
    //        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
    //        apiResponse.setSuccess(true);
    //        apiResponse.setResult(userService.createUser(request));
    //        return apiResponse;
    //    }
    @PostMapping("/create-customer")
    ApiResponse<CreateCustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CreateCustomerResponse customer = customerService.createCustomer(request);
        return ApiResponse.<CreateCustomerResponse>builder()
                .success(true)
                .result(customer)
                .build();
    }

    @PutMapping("update-customer/{customerId}")
    ApiResponse<UpdateCustomerResponse> updateCustomer(
            @PathVariable int customerId,@Valid @RequestBody UpdateCustomerRequest request) {
        UpdateCustomerResponse updateCustomerResponse = customerService.updateCustomer(customerId, request);
        return ApiResponse.<UpdateCustomerResponse>builder()
                .success(true)
                .result(updateCustomerResponse)
                .build();
    }

    @PutMapping("delete-customer/{customerId}")
    ApiResponse<String> deleteCustomer(@PathVariable int customerId) {
        return ApiResponse.<String>builder()
                .success(true)
                .result(customerService.customerDelete(customerId))
                .build();
    }

    @GetMapping("view-customer-detail")
    ApiResponse<CustomerResponse> viewCustomerDetail(@RequestParam
                                                     @Pattern(regexp = "^(0[35789][0-9]{8}|\\+84[35789][0-9]{8})$", message = "CUSTOMER_PHONE_INVALID") String phoneNumber) {
        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .result(customerService.getCustomerByPhoneNumber(phoneNumber))
                .build();
    }

    @GetMapping("/search-customer")
    public ApiResponse<Page<CustomerResponse>> searchCustomers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String citizenId,
            Pageable pageable) {
        Page<CustomerResponse> result = customerService.searchCustomers(fullName, phoneNumber, citizenId, pageable);

        return ApiResponse.<Page<CustomerResponse>>builder()
                .success(true)
                .result(result)
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<CustomerResponse> myInfo() {
        CustomerResponse result = customerService.getMyInfo();
        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .result(result)
                .build();
    }

    @PutMapping("/update-myinfo")
    public ApiResponse<UpdateCustomerResponse> updateMyInfo(@Valid @RequestBody UpdateCustomerRequest request) {
        UpdateCustomerResponse result = customerService.updateMyInfo(request);
        return ApiResponse.<UpdateCustomerResponse>builder()
                .success(true)
                .result(result)
                .build();
    }
}
