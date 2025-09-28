package com.vti.VietBank.service;

import com.vti.VietBank.dto.request.account.CreateAccountRequest;
import com.vti.VietBank.dto.request.account.UpdateAccountRequest;
import com.vti.VietBank.dto.response.account.CreateAccountResponse;
import com.vti.VietBank.dto.response.account.CustomerAccountResponse;
import com.vti.VietBank.dto.response.account.UpdateAccountResponse;

public interface IAccountService {

    // Staff taọ account cho customer
    public CreateAccountResponse createAccount(CreateAccountRequest request);


    // Staff update account cho customer (thêm tiền vào tài khoản)
    public UpdateAccountResponse updateAccount(String accountNumber, UpdateAccountRequest request);

    // Staff khóa tài khoản customer
    public String deActiveAccount(String accountNumber);


    // Customer lấy thông tin tài khoản của mình
    public CustomerAccountResponse getMyAccount();
}
