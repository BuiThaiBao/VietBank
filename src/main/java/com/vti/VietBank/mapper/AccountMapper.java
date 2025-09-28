package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.vti.VietBank.dto.request.account.CreateAccountRequest;
import com.vti.VietBank.dto.request.account.UpdateAccountRequest;
import com.vti.VietBank.dto.response.account.AccountResponse;
import com.vti.VietBank.entity.Account;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    AccountResponse toResponse(Account account);

    Account toEntity(CreateAccountRequest request);
    

    void updateAccount(@MappingTarget Account account, UpdateAccountRequest request);
}
