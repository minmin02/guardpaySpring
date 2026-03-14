package com.example.guardpay.domain.beneficiaries.dto.res;

public record TransferResponse(
        Long transactionId,
        int updatedBalance,
        int reward
) {}