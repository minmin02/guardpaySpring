package com.example.guardpay.domain.beneficiaries.converter;

import com.example.guardpay.domain.beneficiaries.dto.res.TransferResponse;
import com.example.guardpay.domain.beneficiaries.entity.Beneficiary;
import com.example.guardpay.domain.beneficiaries.entity.TransferHistory;
import com.example.guardpay.domain.member.entity.Member;

import java.time.LocalDateTime;

public class TransferConverter {
    public static TransferHistory toEntity(Member member, Beneficiary beneficiary, int amount, int reward) {
        return TransferHistory.builder()
                .member(member)
                .beneficiaryNickname(beneficiary.getNickname())
                .bankName(beneficiary.getBankName())
                .accountNumber(beneficiary.getAccountNumber())
                .amount(amount)
                .reward(reward)
                .updatedBalance(member.getPoints()) // 이미 보상이 합산된 포인트
                .status("COMPLETED")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static TransferResponse toResponse(TransferHistory history) {
        return new TransferResponse(
                history.getTransactionId(),
                history.getUpdatedBalance(),
                history.getReward()
        );
    }
}
