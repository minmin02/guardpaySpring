package com.example.guardpay.domain.quiz.repository;

import com.example.guardpay.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q WHERE q.category.categoryId = :categoryId AND q.level = :level")
    List<Quiz> findByCategoryIdAndLevel(Long categoryId, int level);

    @Query("SELECT q FROM Quiz q WHERE q.category.categoryId = :categoryId")
    List<Quiz> findByCategoryId(Long categoryId);

    long countByCategory_CategoryId(Long categoryId);

}



