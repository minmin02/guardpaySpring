package com.example.guardpay.domain.shop.service;

import com.example.guardpay.domain.shop.dto.ProductResponseDto;
import com.example.guardpay.domain.shop.entity.Product;
import com.example.guardpay.domain.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 🔥 상품 리스트 조회
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> ProductResponseDto.builder()
                        .id(p.getProductId())
                        .brand(p.getBrand())
                        .name(p.getName())
                        .category(p.getCategory())
                        .pricePoint(Integer.valueOf(p.getPricePoint()))
                        .thumbnail(p.getThumbnail())
                        .build()
                )
                .toList();
    }

    // 🔥 개별 상품 조회
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
