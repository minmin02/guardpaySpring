package com.example.guardpay.domain.member.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TERM {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer termId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 250)
    private String content;

    @Column(nullable = false)
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)//회원 테이블과 N:1관계매핑
    @JoinColumn(name="member_id")
    private Member member;





}
