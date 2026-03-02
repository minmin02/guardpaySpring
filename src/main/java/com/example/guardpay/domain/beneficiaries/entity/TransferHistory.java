package com.example.guardpay.domain.beneficiaries.entity;

import com.example.guardpay.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String beneficiaryNickname;
    private String bankName;
    private String accountNumber;
    private int amount;
    private int reward;
    private int updatedBalance;
    private String status; // COMPLETED, FAILED
    private LocalDateTime timestamp;
}
