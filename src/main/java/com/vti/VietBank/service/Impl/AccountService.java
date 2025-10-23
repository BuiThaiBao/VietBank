package com.vti.VietBank.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vti.VietBank.dto.request.account.CreateAccountRequest;
import com.vti.VietBank.dto.request.account.UpdateAccountRequest;
import com.vti.VietBank.dto.response.account.AccountResponse;
import com.vti.VietBank.dto.response.account.CreateAccountResponse;
import com.vti.VietBank.dto.response.account.CustomerAccountResponse;
import com.vti.VietBank.dto.response.account.UpdateAccountResponse;
import com.vti.VietBank.entity.Account;
import com.vti.VietBank.entity.Customer;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.mapper.AccountMapper;
import com.vti.VietBank.repository.IAccountRepository;
import com.vti.VietBank.repository.ICustomerRepository;
import com.vti.VietBank.service.IAccountService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    ICustomerRepository customerRepository;
    IAccountRepository accountRepository;
    AccountMapper accountMapper;

    @Override
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT')")
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        Customer customer = customerRepository
                .findByCitizenId(request.getCitizenId())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        String uniqueAccountNumber = generateUniqueAccountNumber();

        Account account = new Account();
        account.setCustomer(customer);
        account.setAccountNumber(uniqueAccountNumber);
        account.setBalance(request.getBalance());

        Account savedAccount = accountRepository.save(account);
        return CreateAccountResponse.builder()
                .citizenId(request.getCitizenId())
                .fullName(savedAccount.getCustomer().getFullName())
                .accountNumber(uniqueAccountNumber)
                .balance(savedAccount.getBalance())
                .createdAt(LocalDate.from(savedAccount.getCreatedAt()))
                .updatedAt(LocalDate.from(savedAccount.getUpdatedAt()))
                .isActive(savedAccount.getIsActive())
                .build();
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT')")
    public UpdateAccountResponse updateAccount(String accountNumber, UpdateAccountRequest request) {
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        accountMapper.updateAccount(account, request);
        var accountBalance = account.getBalance();
        var newAccountBalance = accountBalance.add(request.getAddMoney());
        account.setBalance(newAccountBalance);

        accountRepository.save(account);
        return UpdateAccountResponse.builder()
                .citizenId(account.getCustomer().getCitizenId())
                .fullName(account.getCustomer().getFullName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .updatedAt(LocalDate.from(account.getUpdatedAt()))
                .isActive(account.getIsActive())
                .build();
    }

    @Override
    @PreAuthorize("hasAuthority('DEACTIVATE_ACCOUNT')")
    public String deActiveAccount(String accountNumber) {
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        account.setIsActive("0");
        accountRepository.save(account);
        return "Account unactive successfully";
    }

    @Override
    public CustomerAccountResponse getMyAccount() {
        var context = SecurityContextHolder.getContext();
        String phoneNumber = context.getAuthentication().getName();
        List<Account> accounts =
                accountRepository.findByPhoneNumber(phoneNumber).stream().toList();
        if (accounts.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        Customer customer = accounts.get(0).getCustomer();

        List<AccountResponse> accountResponse = accounts.stream()
                .map(account -> AccountResponse.builder()
                        .accountNumber(account.getAccountNumber())
                        .balance(account.getBalance())
                        .isActive(account.getIsActive())
                        .createdAt(LocalDate.from(account.getCreatedAt()))
                        .updatedAt(LocalDate.from(account.getUpdatedAt()))
                        .build())
                .toList();
        return CustomerAccountResponse.builder()
                .citizenId(customer.getCitizenId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .isActive(customer.getIsActive())
                .createdAt(LocalDate.from(customer.getCreatedAt()))
                .updatedAt(LocalDate.from(customer.getUpdatedAt()))
                .account(accountResponse)
                .build();
    }






    // Tạo stk duy nhất
    private String generateUniqueAccountNumber() {
        String newAccountNumber;
        int maxAttempts = 100; // Giới hạn số lần thử để tránh vòng lặp vô tận

        for (int i = 0; i < maxAttempts; i++) {
            // Gọi hàm sinh số ngẫu nhiên
            newAccountNumber = AccountNumberGenerator.generateRandomNumber(ACCOUNT_NUMBER_LENGTH);

            // Kiểm tra trong CSDL
            if (!accountRepository.existsByAccountNumber(newAccountNumber)) {
                return newAccountNumber; // Số duy nhất được tìm thấy
            }
        }

        // Nếu sau nhiều lần thử mà vẫn trùng (rất hiếm), ném lỗi
        throw new RuntimeException("Không thể tạo số tài khoản duy nhất sau " + maxAttempts + " lần thử.");
    }


    // Tạo sô tài khoản
    private class AccountNumberGenerator {
        // Tạo số tài khoản 10 chữ số ngẫu nhiên
        public static String generateRandomNumber(int length) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);

            // Đảm bảo chữ số đầu tiên không phải là 0
            sb.append(random.nextInt(9) + 1);

            for (int i = 1; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }
    }
}
