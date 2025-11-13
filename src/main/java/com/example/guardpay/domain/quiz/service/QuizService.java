package com.example.guardpay.domain.quiz.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.entity.*;
import com.example.guardpay.domain.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final CategoryRepository categoryRepository;
    private final QuizRepository quizRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private final ProgressRepository progressRepository;
    private final MemberLevelRepository memberLevelRepository;
    private final MemberRepository memberRepository; // ✅ 추가

    // ✅ 1. 카테고리 목록 조회
    public Map<String, Object> getCategories() {
        List<Map<String, Object>> categories = categoryRepository.findAll().stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("categoryId", c.getCategoryId());
                    map.put("name", c.getName());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "카테고리 목록 조회 성공",
                "data", Map.of("categories", categories)
        );
    }

    // ✅ 2. 카테고리별 퀴즈 목록 조회
    public Map<String, Object> getQuizzesByCategory(Long categoryId, int level) {
        List<Quiz> quizzes = quizRepository.findByCategoryIdAndLevel(categoryId, level);

        List<Map<String, Object>> quizList = quizzes.stream()
                .map(q -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("quizId", q.getQuizId());
                    map.put("question", q.getQuestion());
                    map.put("level", q.getLevel());
                    map.put("point", q.getPoint());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "퀴즈 목록 조회 성공",
                "data", Map.of("quizzes", quizList)
        );
    }

    public Map<String, Object> getQuizzesByCategory(Long categoryId) {
        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);

        List<Map<String, Object>> quizList = quizzes.stream()
                .map(q -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("quizId", q.getQuizId());
                    map.put("question", q.getQuestion());
                    map.put("level", q.getLevel());
                    map.put("point", q.getPoint());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "퀴즈 목록 조회 성공",
                "data", Map.of("quizzes", quizList)
        );
    }

    // ✅ 3. 퀴즈 상세 조회
    public Map<String, Object> getQuizDetail(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        List<String> options = quizOptionRepository.findByQuiz_QuizId(quizId).stream()
                .map(QuizOption::getOptionText)
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("quizId", quiz.getQuizId());
        data.put("question", quiz.getQuestion());
        data.put("options", options);
        data.put("point", quiz.getPoint());

        return Map.of(
                "status", 200,
                "message", "퀴즈 상세 조회 성공",
                "data", data
        );
    }

public Map<String, Object> submitAnswer(Long memberId, Long quizId, Long selectedOptionId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));
        QuizOption selected = quizOptionRepository.findById(selectedOptionId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 보기 ID가 존재하지 않습니다."));

        boolean isCorrect = selected.getIsCorrect();
        int gainExp = isCorrect ? quiz.getPoint() : 0;

        // ✅ 정답일 경우 Member 포인트 반영
        if (isCorrect) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            member.addExp(gainExp);
            memberRepository.save(member);
        }

        // ✅ 풀이 기록 저장
        QuizHistory history = QuizHistory.builder()
                .member(memberRepository.findById(memberId).orElseThrow())
                .quiz(quiz)
                .gainExp(gainExp)
                .answerAt(java.time.LocalDateTime.now())
                .build();
        quizHistoryRepository.save(history);

        Map<String, Object> data = new HashMap<>();
        data.put("isCorrect", isCorrect);
        data.put("gainExp", gainExp);

        return Map.of(
                "status", 200,
                "message", isCorrect ? "정답입니다! 포인트가 적립되었습니다." : "오답입니다.",
                "data", data
        );
    }

    // ✅ 5. 퀴즈 기록 조회
    public Map<String, Object> getQuizHistory(Long memberId) {
        List<Map<String, Object>> history = quizHistoryRepository.findByMember_MemberId(memberId)
                .stream()
                .map(h -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("quizId", h.getQuiz().getQuizId());
                    map.put("question", h.getQuiz().getQuestion());
                    map.put("gainExp", h.getGainExp());
                    map.put("answerAt", h.getAnswerAt());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "퀴즈 기록 조회 성공",
                "data", Map.of("history", history)
        );
    }

    // ✅ 6. 진행률 조회
    public Map<String, Object> getProgress(Long memberId) {
        List<Map<String, Object>> progressList = progressRepository.findByMember_MemberId(memberId)
                .stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("categoryId", p.getCategory().getCategoryId());
                    map.put("categoryName", p.getCategory().getName());
                    map.put("progress", p.getProgress());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "진행률 조회 성공",
                "data", Map.of("progress", progressList)
        );
    }

    // ✅ 7. 레벨 조회
    public Map<String, Object> getLevels(Long memberId) {
        List<Map<String, Object>> levels = memberLevelRepository.findByMember_MemberId(memberId)
                .stream()
                .map(l -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("categoryId", l.getCategory().getCategoryId());
                    map.put("categoryName", l.getCategory().getName());
                    map.put("level", l.getLevel());
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "status", 200,
                "message", "레벨 조회 성공",
                "data", Map.of("levels", levels)
        );
    }
}
