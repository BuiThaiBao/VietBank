package com.vti.VietBank.mapper;

import com.vti.VietBank.dto.request.CreateAccountRequest;
import com.vti.VietBank.dto.response.AccountResponse;
import com.vti.VietBank.entity.Account;

public interface AccountMapper {
    AccountResponse toResponse(Account account);

    Account toEntity(CreateAccountRequest request);
}
