package com.example.guardpay.domain.shop.converter;

import com.example.guardpay.domain.shop.dto.ProductResponseDto;
import com.example.guardpay.domain.shop.entity.Product;


public class ProductConverter {

    public static ProductResponseDto toResponseDto(Product p){
        return  ProductResponseDto.builder()
                .id(p.getProductId())
                .brand(p.getBrand())
                .name(p.getName())
                .category(p.getCategory())
                .pricePoint(Integer.valueOf(p.getPricePoint()))
                .thumbnail(p.getThumbnail())
                .build();
    }

}
