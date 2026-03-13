package com.example.guardpay.domain.shop.repository;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeLogRepository extends JpaRepository<ExchangeLog, Long> {

    List<ExchangeLog> findByMember(Member member);

    @Query("select l from ExchangeLog l"
            +"join fetch l.product"+ // log를 가져올 때 product도 한 몸으로 가져옴
            "where l.member=:member")
    List<ExchangeLog>findByMemberWithProduct( Member member);


}
