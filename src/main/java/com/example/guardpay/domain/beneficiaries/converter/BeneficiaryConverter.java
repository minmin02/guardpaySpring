package com.example.guardpay.domain.beneficiaries.converter;

import com.example.guardpay.domain.beneficiaries.dto.res.BeneficiaryResponseDto;
import com.example.guardpay.domain.beneficiaries.entity.Beneficiary;

import java.util.List;

public class BeneficiaryConverter {

    public static BeneficiaryResponseDto toResponseDto(Beneficiary beneficiary) {
        return new BeneficiaryResponseDto(
                beneficiary.getId(),
                beneficiary.getNickname(),
                beneficiary.getBankName(),
                beneficiary.getAccountNumber(),
                beneficiary.getAccountHolderName()
        );
    }

    public static List<BeneficiaryResponseDto> toResponseDtoList(List<Beneficiary> beneficiaries) {
        return beneficiaries.stream()
                .map(BeneficiaryConverter::toResponseDto)
                .toList();
    }

}
