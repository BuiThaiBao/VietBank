package com.vti.VietBank.mapper;

import org.mapstruct.Mapper;

import com.vti.VietBank.dto.request.CreateTransactionRequest;
import com.vti.VietBank.dto.response.TransactionResponse;
import com.vti.VietBank.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);

    Transaction toEntity(CreateTransactionRequest request);
}
