package com.example.guardpay.domain.quiz.repository;

import com.example.guardpay.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    List<QuizHistory> findByMember_MemberId(Long memberId);
}