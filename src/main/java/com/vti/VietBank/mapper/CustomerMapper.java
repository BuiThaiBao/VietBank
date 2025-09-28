package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vti.VietBank.dto.request.customer.UpdateCustomerRequest;
import com.vti.VietBank.dto.response.customer.CustomerResponse;
import com.vti.VietBank.dto.response.customer.UpdateCustomerResponse;
import com.vti.VietBank.entity.Customer;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);

    // Update Customer Response
    UpdateCustomerResponse toUpdateResponse(Customer customer);

    @Mapping(target = "dob", source = "dob", dateFormat = "yyyy-MM-dd",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, UpdateCustomerRequest request);
}
