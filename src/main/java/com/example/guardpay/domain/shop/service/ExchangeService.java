package com.example.guardpay.domain.shop.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.shop.converter.ShopConverter;
import com.example.guardpay.domain.shop.dto.ExchangeHistoryItemDto;
import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import com.example.guardpay.domain.shop.entity.Product;
import com.example.guardpay.domain.shop.enums.ShopErrorCode;
import com.example.guardpay.domain.shop.exeption.ShopException;
import com.example.guardpay.domain.shop.repository.ExchangeLogRepository;
import com.example.guardpay.domain.video.converter.PreventionVideoConverter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ProductService productService;
    private final MemberRepository memberRepository;
    private final ExchangeLogRepository exchangeLogRepository;
    //엔티티의 생명 주기 관리
    // entityManager.persist(member);  영속성 컨텍스트에 저장
    private final EntityManager entityManager;

    // Mock 쿠폰 코드 생성기
    private String generateMockCouponCode() {
        return "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional
    public ExchangeResponseDto exchangeProduct(Long memberId, Long productId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Product product = productService.getProduct(productId);

        //  포인트 검증
        if (member.getPoints() < product.getPricePoint()) {
            throw new ShopException(ShopErrorCode.INSUFFICIENT_POINTS);
        }

        int updatedBalance = member.getPoints() - product.getPricePoint();
        member.setPoints(updatedBalance);
        memberRepository.save(member);

        String couponCode = generateMockCouponCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validUntil = now.plusMonths(6);

        // 교환 내역 저장
        ExchangeLog exchangeLog = ExchangeLog.createExchangeLog(member, product, couponCode, validUntil);
        exchangeLogRepository.save(exchangeLog);

        // 응답 DTO 생성
        return ShopConverter.toResponseDto(product, couponCode, validUntil);
    }

    // 교환 내역 조회
    public List<ExchangeHistoryItemDto> getExchangeHistory(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<ExchangeHistoryItemDto> responses=exchangeLogRepository.findByMemberWithProduct(member).stream()
                .map(log -> PreventionVideoConverter.toDto(log)).toList();

        return responses;
    }

}
