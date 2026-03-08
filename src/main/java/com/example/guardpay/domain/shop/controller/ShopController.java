package com.example.guardpay.domain.shop.controller;

import com.example.guardpay.domain.shop.dto.ExchangeHistoryItemDto;
import com.example.guardpay.domain.shop.dto.ExchangeResponseDto;
import com.example.guardpay.domain.shop.dto.ProductResponseDto;
import com.example.guardpay.domain.shop.service.ExchangeService;
import com.example.guardpay.domain.shop.service.ProductService;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import com.example.guardpay.global.jwt.MemberUserDetails;
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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ProductService productService;
    private final ExchangeService exchangeService;

    // 1. 상품 목록 조회
    @GetMapping("/products")
    public ApiResponse<Page<ProductResponseDto>> getProducts(@PageableDefault(size = 10, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable
    ,@AuthenticationPrincipal MemberUserDetails userDetails) {

        Page<ProductResponseDto> products=productService.getAllProducts(pageable);
        return ApiResponse.ok(products);
    }

    // 2. 상품 교환
    @PostMapping("/exchange/{productId}")
    public ApiResponse<ExchangeResponseDto> exchangeProduct(
            @PathVariable Long productId
            ,@AuthenticationPrincipal MemberUserDetails userDetails
    ) {
        Long memberId= userDetails.getMember().getMemberId();
        ExchangeResponseDto response = exchangeService.exchangeProduct(memberId, productId);
        return ApiResponse.ok(response);
    }

    //3. 교환 내역 조회
    @GetMapping("/history")
    public ApiResponse<List<ExchangeHistoryItemDto>> getExchangeHistory(
           @AuthenticationPrincipal MemberUserDetails userDetails
    ) {
        Long memberId=userDetails.getMember().getMemberId();
        List<ExchangeHistoryItemDto>responses= exchangeService.getExchangeHistory(memberId);
        return ApiResponse.ok(responses);
    }

}
