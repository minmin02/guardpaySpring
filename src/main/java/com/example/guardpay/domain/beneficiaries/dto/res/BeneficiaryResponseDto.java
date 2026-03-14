package com.example.guardpay.domain.beneficiaries.dto.res;

public record BeneficiaryResponseDto(
        Long id,
        String nickname,
        String bankName,
        String accountNumber,
        String accountHolderName
) {}