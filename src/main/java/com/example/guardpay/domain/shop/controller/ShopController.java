package com.example.guardpay.domain.shop.controller;

import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.service.ExchangeService;
import com.example.guardpay.domain.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class ShopController {

    private final ProductService productService;
    private final ExchangeService exchangeService;

    // 📌 1. 상품 목록 조회
    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "상품 목록 조회 성공",
                "data", productService.getAllProducts()
        ));
    }

    // 📌 2. 상품 교환
    @PostMapping("/exchange/{productId}")
    public ResponseEntity<?> exchangeProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token
    ) {
        Long memberId = extractUserIdFromToken(token);

        ExchangeResponseDto dto = exchangeService.exchangeProduct(memberId, productId);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "상품 교환 완료",
                "data", dto
        ));
    }

    // 📌 3. 교환 내역 조회
    @GetMapping("/history")
    public ResponseEntity<?> getExchangeHistory(
            @RequestHeader("Authorization") String token
    ) {
        Long memberId = extractUserIdFromToken(token);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "교환 내역 조회 성공",
                "data", Map.of("exchanges", exchangeService.getExchangeHistory(memberId))
        ));
    }

    // TODO: 실제 JWT 기반으로 교체
    private Long extractUserIdFromToken(String token) {
        return 1L;
    }
}
