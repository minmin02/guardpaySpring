package com.example.guardpay.domain.beneficiaries.service;

import com.example.guardpay.domain.beneficiaries.entity.*;
import com.example.guardpay.domain.beneficiaries.repository.*;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final MemberRepository memberRepository;

    public Map<String, Object> getRandomBeneficiaries() {
        List<Beneficiary> list = beneficiaryRepository.findRandomBeneficiaries();
        List<Map<String, Object>> result = new ArrayList<>();

        list.stream().limit(4).forEach(b -> {
            result.add(Map.of(
                    "id", b.getId(),
                    "nickname", b.getNickname(),
                    "bankName", b.getBankName(),
                    "accountNumber", b.getAccountNumber(),
                    // 추가
                    "accountHolderName", b.getAccountHolderName()
            ));
        });

        return Map.of(
                "status", 200,
                "message", "가상 계좌 조회 성공",
                "data", result
        );
    }

    public Map<String, Object> getBeneficiaryDetail(Long id) {
        Beneficiary b = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계좌입니다."));
        return Map.of(
                "status", 200,
                "message", "계좌 상세 조회 성공",
                "data", Map.of(
                        "nickname", b.getNickname(),
                        "bankName", b.getBankName(),
                        "accountNumber", b.getAccountNumber(),
                        "accountHolderName", b.getAccountHolderName()
                )
        );
    }

    public Map<String, Object> transfer(Long memberId, Long beneficiaryId, int amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보가 없습니다."));
        Beneficiary b = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 송금 대상입니다."));

        if (member.getBalance() < amount)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잔액이 부족합니다.");

        int reward = 100;
        int updatedBalance = member.getBalance() + reward;
        member.setBalance(updatedBalance);
        memberRepository.save(member);

        TransferHistory history = TransferHistory.builder()
                .member(member)
                .beneficiaryNickname(b.getNickname())
                .bankName(b.getBankName())
                .accountNumber(b.getAccountNumber())
                .amount(amount)
                .reward(reward)
                .updatedBalance(updatedBalance)
                .status("COMPLETED")
                .timestamp(LocalDateTime.now())
                .build();
        transferHistoryRepository.save(history);

        return Map.of(
                "status", 200,
                "message", "송금이 완료되었습니다.",
                "data", Map.of(
                        "transactionId", history.getTransactionId(),
                        "updatedBalance", updatedBalance,
                        "reward", reward
                )
        );
    }
}
