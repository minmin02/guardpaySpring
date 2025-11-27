package com.example.guardpay.domain.shop.repository;

import com.example.guardpay.domain.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
