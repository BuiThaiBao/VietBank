package com.vti.VietBank.controller;


import com.vti.VietBank.dto.request.transaction.TransferMoneyRequest;
import com.vti.VietBank.dto.response.ApiResponse;
import com.vti.VietBank.dto.response.transaction.TransferMoneyResponse;
import com.vti.VietBank.service.ITransactionService;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    ITransactionService transactionService;

    @PostMapping("/transfer")
    public ApiResponse<TransferMoneyResponse> createTransaction(@RequestBody TransferMoneyRequest request) {
        TransferMoneyResponse result = transactionService.transferMoney(request);
        return ApiResponse.<TransferMoneyResponse>builder()
                .success(true)
                .result(result)
                .build();
    }

}
