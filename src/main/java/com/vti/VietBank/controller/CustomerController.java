package com.vti.VietBank.controller;

import com.vti.VietBank.dto.request.CreateCustomerRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.CreateCustomerResponse;
import com.vti.VietBank.service.ICustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ApiResponse<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request){
        CreateCustomerResponse customer = customerService.createCustomer(request);
        return ApiResponse.<CreateCustomerResponse>builder()
                .success(true)
                .result(customer)
                .build();
    }
}
