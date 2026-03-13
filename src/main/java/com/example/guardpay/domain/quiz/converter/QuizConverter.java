package com.example.guardpay.domain.quiz.converter;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.quiz.dto.res.*;
import com.example.guardpay.domain.quiz.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuizConverter {
    public static CategoryResponseDto toResponseDto(Category c) {
        return new CategoryResponseDto(c.getCategoryId(), c.getName());
    }

    // 단건 변환
    public static QuizResponseDto toDto(Quiz quiz) {
        return new QuizResponseDto(
                quiz.getQuizId(),
                quiz.getQuestion(),
                quiz.getLevel(),
                quiz.getPoint()
        );
    }

    // 리스트 변환
    public static QuizListResponse toListResponse(List<Quiz> quizzes) {
        List<QuizResponseDto> dtos = quizzes.stream()
                .map(QuizConverter::toDto)
                .toList();
        return new QuizListResponse(dtos);
    }

    public static QuizDetailResponse toDetailResponse(Quiz quiz, List<QuizOption> options) {
        List<QuizOptionDto> optionDtos = options.stream()
                .map(opt -> new QuizOptionDto(opt.getOptionId(), opt.getOptionText()))
                .toList();

        return new QuizDetailResponse(
                quiz.getQuizId(),
                quiz.getQuestion(),
                quiz.getPoint(),
                optionDtos
        );
    }

    public static Progress toInitialProgress(Member member, Category category) {
        return Progress.builder()
                .member(member)
                .category(category)
                .progress(0)
                .updateAt(LocalDateTime.now())
                .build();
    }

    public static QuizHistory toHistoryEntity(Member member, Quiz quiz, int gainExp) {
        return QuizHistory.builder()
                .member(member)
                .quiz(quiz)
                .gainExp(gainExp)
                .answerAt(LocalDateTime.now())
                .build();
    }

    public static QuizHistoryDto toHistoryDto(QuizHistory history) {
        return new QuizHistoryDto(
                history.getQuiz().getQuizId(),
                history.getQuiz().getQuestion(),
                history.getGainExp(),
                history.getAnswerAt()
        );
    }

    public static List<QuizHistoryDto> toHistoryDtoList(List<QuizHistory> histories) {
        return histories.stream()
                .map(QuizConverter::toHistoryDto)
                .toList();
    }

    // 진행률 변환
    public static List<ProgressResponseDto> toProgressDtoList(List<Progress> progressList) {
        return progressList.stream()
                .map(p -> new ProgressResponseDto(
                        p.getCategory().getCategoryId(),
                        p.getCategory().getName(),
                        p.getProgress()))
                .toList();
    }

    // 레벨 변환
    public static List<MemberLevelResponseDto> toLevelDtoList(List<MemberLevel> levels) {
        return levels.stream()
                .map(l -> new MemberLevelResponseDto(
                        l.getCategory().getCategoryId(),
                        l.getCategory().getName(),
                        l.getLevel()))
                .toList();
    }


}
