package com.example.guardpay;

import com.example.guardpay.domain.GeminiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GeminiConfig.class)
public class GuardpayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuardpayApplication.class, args);
    }
}