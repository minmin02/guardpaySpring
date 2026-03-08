package com.example.guardpay.domain.shop.controller;

import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.dto.ProductResponseDto;
import com.example.guardpay.domain.shop.service.ExchangeService;
import com.example.guardpay.domain.shop.service.ProductService;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import com.example.guardpay.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ProductService productService;
    private final ExchangeService exchangeService;
    private final JwtTokenProvider jwtTokenProvider;

    // 1. 상품 목록 조회
    @GetMapping("/products")
    public ApiResponse<Page<ProductResponseDto>> getProducts(@PageableDefault(size = 10, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable
    ,@AuthenticationPrincipal UserDetails userDetails) {

        Page<ProductResponseDto> products=productService.getAllProducts(pageable);
        return ApiResponse.ok(products);
    }

    // 2. 상품 교환
    @PostMapping("/exchange/{productId}")
    public ResponseEntity<?> exchangeProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token
    ) {
        String jwtToken = token.replace("Bearer ", "");
        Long memberId = jwtTokenProvider.getMemberId(jwtToken);

        ExchangeResponseDto dto = exchangeService.exchangeProduct(memberId, productId);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "상품 교환 완료",
                "data", dto
        ));
    }

    //3. 교환 내역 조회
    @GetMapping("/history")
    public ResponseEntity<?> getExchangeHistory(
            @RequestHeader("Authorization") String token
    ) {
        String jwtToken = token.replace("Bearer ", "");
        Long memberId = jwtTokenProvider.getMemberId(jwtToken);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "교환 내역 조회 성공",
                "data", Map.of("exchanges", exchangeService.getExchangeHistory(memberId))
        ));
    }

//    // TODO: 실제 JWT 기반으로 교체
//    private Long extractUserIdFromToken(String token) {
//        return 13L;
//    }
}
