package com.example.guardpay.domain.quiz.service;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.converter.QuizConverter;
import com.example.guardpay.domain.quiz.dto.res.*;
import com.example.guardpay.domain.quiz.entity.*;

import com.example.guardpay.domain.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizHistoryService {
    private final QuizHistoryRepository quizHistoryRepository;
    private final ProgressRepository progressRepository;
    private final MemberLevelRepository memberLevelRepository;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveQuizHistory(Member member, Quiz quiz, int gainExp) {
        quizHistoryRepository.save(QuizConverter.toHistoryEntity(member, quiz, gainExp));
    }

    @Transactional
    public void updateProgress(Member member, Quiz quiz) {
        Long categoryId = quiz.getCategory().getCategoryId();
        long totalCount = quizRepository.countByCategory_CategoryId(categoryId);

        Progress progress = progressRepository
                .findByMember_MemberIdAndCategory_CategoryId(member.getMemberId(), categoryId)
                .orElseGet(() -> QuizConverter.toInitialProgress(member, quiz.getCategory()));

        int increase = (int) Math.round((1.0 / totalCount) * 100.0);
        progress.updateProgress(increase);
        progressRepository.save(progress);
    }

    public boolean isAlreadySolved(Long memberId, Long quizId) {
        return quizHistoryRepository.existsByMember_MemberIdAndQuiz_QuizIdAndGainExpGreaterThan(memberId, quizId, 0);
    }

    public List<QuizHistoryDto> getQuizHistory(Long memberId) {
        checkMember(memberId);
        return QuizConverter.toHistoryDtoList(quizHistoryRepository.findByMember_MemberId(memberId));
    }

    public List<ProgressResponseDto> getProgress(Long memberId) {
        checkMember(memberId);
        return QuizConverter.toProgressDtoList(progressRepository.findByMember_MemberId(memberId));
    }

    public List<MemberLevelResponseDto> getLevels(Long memberId) {
        checkMember(memberId);
        return QuizConverter.toLevelDtoList(memberLevelRepository.findByMember_MemberId(memberId));
    }

    private void checkMember(Long id) {
        if (!memberRepository.existsById(id)) throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}