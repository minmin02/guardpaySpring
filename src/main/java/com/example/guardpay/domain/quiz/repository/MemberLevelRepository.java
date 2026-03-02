package com.example.guardpay.domain.quiz.repository;

import com.example.guardpay.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long> {
    List<MemberLevel> findByMember_MemberId(Long memberId);
}
