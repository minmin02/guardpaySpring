package com.example.guardpay.domain.shop.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.shop.dto.ExchangeHistoryItemDto;
import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import com.example.guardpay.domain.shop.entity.Product;
import com.example.guardpay.domain.shop.repository.ExchangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    // 🔥 Mock 쿠폰 코드 생성기
    private String generateMockCouponCode() {
        return "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // 🔥 상품 교환 (구매)
    public ExchangeResponseDto exchangeProduct(Long memberId, Long productId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        Product product = productService.getProduct(productId);

        if (member.getBalance() < product.getPricePoint())
            throw new IllegalArgumentException("잔액이 부족합니다.");

        int updatedBalance = member.getBalance() - product.getPricePoint();
        member.setBalance(updatedBalance);
        memberRepository.save(member);

        String couponCode = generateMockCouponCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validUntil = now.plusMonths(6);

        // 교환 내역 저장
        ExchangeLog exchangeLog = exchangeLogRepository.save(
                ExchangeLog.builder()
                        .member(member)
                        .product(product)
                        .pointsUsed(Integer.valueOf(product.getPricePoint()))
                        .status("USEABLE")
                        .couponCode(couponCode)
                        .validUntil(validUntil)
                        .exchangedAt(now)
                        .build()
        );

        // 응답 DTO 생성
        return ExchangeResponseDto.builder()
                .productId(productId)
                .brand(product.getBrand())
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .couponCode(couponCode)
                .validUntil(validUntil.toString())
                .build();
    }

    // 🔥 교환 내역 조회
    public List<ExchangeHistoryItemDto> getExchangeHistory(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        return exchangeLogRepository.findByMember(member).stream()
                .map(log -> ExchangeHistoryItemDto.builder()
                        .exchangeId(log.getExchangeId())
                        .productName(log.getProduct().getName())
                        .pointsUsed(log.getPointsUsed())
                        .status(log.getStatus())
                        .exchangedAt(log.getExchangedAt().toString())
                        .build()
                )
                .toList();
    }
}
