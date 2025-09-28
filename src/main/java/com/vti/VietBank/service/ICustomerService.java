package com.vti.VietBank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vti.VietBank.dto.request.customer.CreateCustomerRequest;
import com.vti.VietBank.dto.request.customer.UpdateCustomerRequest;
import com.vti.VietBank.dto.response.customer.CreateCustomerResponse;
import com.vti.VietBank.dto.response.customer.CustomerResponse;
import com.vti.VietBank.dto.response.customer.UpdateCustomerResponse;

public interface ICustomerService {
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request);

    public UpdateCustomerResponse updateCustomer(int customerId, UpdateCustomerRequest request);

    public String customerDelete(int customerId);

    public CustomerResponse getCustomerByPhoneNumber(String phoneNumber);

    public Page<CustomerResponse> searchCustomers(
            String fullName, String phoneNumber, String citizenId, Pageable pageable);

    public CustomerResponse getMyInfo();

    public UpdateCustomerResponse updateMyInfo(UpdateCustomerRequest request);
}
