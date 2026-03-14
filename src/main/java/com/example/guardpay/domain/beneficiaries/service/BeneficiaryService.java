package com.example.guardpay.domain.beneficiaries.service;

import com.example.guardpay.domain.beneficiaries.converter.BeneficiaryConverter;
import com.example.guardpay.domain.beneficiaries.converter.TransferConverter;
import com.example.guardpay.domain.beneficiaries.dto.res.BeneficiaryResponseDto;
import com.example.guardpay.domain.beneficiaries.dto.res.TransferResponse;
import com.example.guardpay.domain.beneficiaries.entity.*;
import com.example.guardpay.domain.beneficiaries.enums.BeneficiaryErrorCode;
import com.example.guardpay.domain.beneficiaries.exception.BeneficiaryException;
import com.example.guardpay.domain.beneficiaries.repository.*;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.enums.QuizErrorCode;
import com.example.guardpay.domain.quiz.exception.QuizException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final MemberRepository memberRepository;


    public List<BeneficiaryResponseDto> getRandomBeneficiaries(Long memberId) {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByActiveTrue();
        List<Beneficiary> shuffled = new ArrayList<>(beneficiaries);
        // 컬렉션 shuffle 메서드 사용
        Collections.shuffle(shuffled);
        return BeneficiaryConverter.toResponseDtoList(shuffled.stream().limit(4).toList());
    }

    public BeneficiaryResponseDto getBeneficiaryDetail(Long memberId, Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new QuizException(QuizErrorCode.BENEFICIARY_NOT_FOUND));
        return BeneficiaryConverter.toResponseDto(beneficiary);
    }

    @Transactional
    public TransferResponse transfer(Long memberId, Long beneficiaryId, int amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryException(BeneficiaryErrorCode.BENEFICIARY_NOT_FOUND));

        member.checkBalance(amount);
        int reward = 100;
        member.addPoints(reward);

        TransferHistory history = TransferConverter.toEntity(member, beneficiary, amount, reward);
        transferHistoryRepository.save(history);

        return TransferConverter.toResponse(history);
    }


}
