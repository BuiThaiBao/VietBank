package com.vti.VietBank.service;

import com.vti.VietBank.dto.request.CreateCustomerRequest;
import com.vti.VietBank.dto.response.CreateCustomerResponse;

public interface ICustomerService {
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request);

}
