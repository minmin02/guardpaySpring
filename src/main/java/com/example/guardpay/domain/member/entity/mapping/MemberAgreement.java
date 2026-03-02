package com.example.guardpay.domain.member.entity.mapping;


import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.entity.TERM;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer memberAgreementId;

    @ManyToOne(fetch = FetchType.LAZY)//회원 테이블과 N:1관계매핑
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)//약관동의 테이블과 N:1관계매핑
    @JoinColumn(name="term_id")
    private TERM term;


}
