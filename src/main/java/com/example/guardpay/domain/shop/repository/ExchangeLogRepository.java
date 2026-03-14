package com.example.guardpay.domain.shop.repository;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeLogRepository extends JpaRepository<ExchangeLog, Long> {

    List<ExchangeLog> findByMember(Member member);

    @Query("""
    select l from ExchangeLog l
    join fetch l.product
    where l.member = :member
""")
    List<ExchangeLog>findByMemberWithProduct( Member member);


}
