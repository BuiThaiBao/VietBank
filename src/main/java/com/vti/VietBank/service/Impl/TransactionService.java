package com.vti.VietBank.service.Impl;

import com.vti.VietBank.dto.request.transaction.TransferMoneyRequest;
import com.vti.VietBank.dto.response.transaction.TransferMoneyResponse;
import com.vti.VietBank.entity.Account;
import com.vti.VietBank.entity.Customer;
import com.vti.VietBank.entity.Transaction;
import com.vti.VietBank.exception.AppException;
import com.vti.VietBank.exception.ErrorCode;
import com.vti.VietBank.repository.IAccountRepository;
import com.vti.VietBank.repository.ICustomerRepository;
import com.vti.VietBank.repository.ITransactionRepository;
import com.vti.VietBank.service.ITransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService implements ITransactionService {

    IAccountRepository accountRepository;
    ITransactionRepository transactionRepository;
    ICustomerRepository customerRepository;

    @Override
    @Transactional
    public TransferMoneyResponse transferMoney(TransferMoneyRequest request) {

        var context = SecurityContextHolder.getContext();
        String phoneNumber = context.getAuthentication().getName();

        Customer customer = customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        Account accountFrom = accountRepository.findByAccountNumber(request.getFromAccount()).orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if(!accountFrom.getCustomer().getId().equals(customer.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_TRANSACTION);
        }
        // Kiểm tra tài khoản có active không
        if (!accountFrom.getIsActive().equals("1")) {
            throw new AppException(ErrorCode.ACCOUNT_INACTIVE);
        }

        Account accountTo = accountRepository.findByAccountNumber(request.getToAccount()).orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!accountTo.getIsActive().equals("1")) {
            throw new AppException(ErrorCode.ACCOUNT_INACTIVE);
        }

        if(accountFrom.getBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException(ErrorCode.TRANSACTION_INVALID_AMOUNT);
        }

        if(accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
            throw new AppException(ErrorCode.SAME_ACCOUNT_TRANSFER);
        }
        if(request.getDescription() == null) {
            request.setDescription(accountFrom.getCustomer().getFullName() + " chuyển khoản ");
        }

        accountFrom.setBalance(accountFrom.getBalance().subtract(request.getAmount()));
        accountTo.setBalance(accountTo.getBalance().add(request.getAmount()));

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        // log transaction
        Transaction tx = new Transaction();
        tx.setFromAccount(accountFrom);
        tx.setToAccount(accountTo);
        tx.setAmount(request.getAmount());
        tx.setDescription(request.getDescription());
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);

        return TransferMoneyResponse.builder()
                .fromAccount(accountFrom.getAccountNumber())
                .toAccount(accountTo.getAccountNumber())
                .amount(request.getAmount())
                .description(request.getDescription())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
