package com.example.guardpay.domain.beneficiaries.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String bankName;
    private String bankCode;
    private String accountNumber;
    private String accountHolderName;
    private boolean active;
}
