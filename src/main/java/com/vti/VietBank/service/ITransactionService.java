package com.vti.VietBank.service;

import com.vti.VietBank.dto.request.transaction.TransferMoneyRequest;
import com.vti.VietBank.dto.response.transaction.TransferMoneyResponse;

public interface ITransactionService {
    public TransferMoneyResponse transferMoney(TransferMoneyRequest request);
}
