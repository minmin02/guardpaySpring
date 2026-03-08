package com.example.guardpay.domain.shop.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.shop.dto.ExchangeHistoryItemDto;
import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import com.example.guardpay.domain.shop.entity.Product;
import com.example.guardpay.domain.shop.repository.ExchangeLogRepository;
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
    // 🔥 Mock 쿠폰 코드 생성기
    private String generateMockCouponCode() {
        return "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional
    public ExchangeResponseDto exchangeProduct(Long memberId, Long productId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));


        entityManager.refresh(member); // 옵션인데 필요없을듯?  영속성 컨텍스트에 저장된 거 DB에서 강제 로드
        System.out.println("🔍 [3] refresh 직후 - member.getPoints(): " + member.getPoints());

        Product product = productService.getProduct(productId);


        if (member.getPoints() < product.getPricePoint())
            throw new IllegalArgumentException("잔액이 부족합니다.");

        int updatedBalance = member.getPoints() - product.getPricePoint();
        member.setPoints(updatedBalance);
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
                        .brandName(log.getProduct().getBrand())
                        .pointsUsed(log.getPointsUsed())
                        .status(log.getStatus())
                        .exchangedAt(log.getExchangedAt().toString())
                        .couponCode(log.getCouponCode())
                        .validUntil(log.getValidUntil().toString())
                        .thumbnail(log.getProduct().getThumbnail())
                        .build()
                )
                .toList();
    }

}
