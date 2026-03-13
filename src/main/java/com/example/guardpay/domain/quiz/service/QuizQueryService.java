package com.example.guardpay.domain.quiz.service;


import com.example.guardpay.domain.quiz.converter.QuizConverter;
import com.example.guardpay.domain.quiz.dto.res.*;
import com.example.guardpay.domain.quiz.entity.*;
import com.example.guardpay.domain.quiz.enums.QuizErrorCode;
import com.example.guardpay.domain.quiz.exception.QuizException;
import com.example.guardpay.domain.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQueryService {
    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;
    private final QuizOptionRepository quizOptionRepository;

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(QuizConverter::toResponseDto)
                .toList();
    }

    public QuizListResponse getQuizzesByCategory(Long categoryId) {
        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);
        return QuizConverter.toListResponse(quizzes);
    }

    public QuizDetailResponse getQuizDetail(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));
        List<QuizOption> options = quizOptionRepository.findByQuiz_QuizId(quizId);
        return QuizConverter.toDetailResponse(quiz, options);
    }
}