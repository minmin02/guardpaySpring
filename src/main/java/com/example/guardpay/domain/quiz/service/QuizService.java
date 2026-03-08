package com.example.guardpay.domain.quiz.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.entity.*;
import com.example.guardpay.domain.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final MemberRepository memberRepository;

    // 1. 카테고리 목록 조회
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

    // 2. 카테고리별 퀴즈 목록 조회
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

    //  3. 퀴즈 상세 조회
    public Map<String, Object> getQuizDetail(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

        //  수정된 부분: List<String> 대신 List<Map<String, Object>> 반환
        List<Map<String, Object>> options = quizOptionRepository.findByQuiz_QuizId(quizId).stream()
                .map(opt -> {
                    Map<String, Object> optionMap = new HashMap<>();
                    optionMap.put("optionId", opt.getOptionId());
                    optionMap.put("optionText", opt.getOptionText());
                    return optionMap;
                })
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

        // 문제 & 선택지 조회
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));
        QuizOption selected = quizOptionRepository.findById(selectedOptionId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 보기 ID가 존재하지 않습니다."));

        boolean isCorrect = selected.getIsCorrect();
        int gainExp = isCorrect ? quiz.getPoint() : 0;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // ==========================================================
        // 1️⃣ 이미 이 문제를 정답 처리한 적 있는지 체크 (중복 progress 방지)
        // ==========================================================
        boolean alreadyCorrect = quizHistoryRepository
                .existsByMember_MemberIdAndQuiz_QuizIdAndGainExpGreaterThan(
                        memberId, quizId, 0
                );

        // ==========================================================
        // 정답 + 처음 맞힘 → EXP 지급
        // ==========================================================
        if (isCorrect && !alreadyCorrect) {
            member.addExp(gainExp);
            memberRepository.save(member);
        }

        // ==========================================================
        //  업데이트 (정답 + 처음 맞힘일 때만)
        // ==========================================================
        if (isCorrect && !alreadyCorrect) {

            Long categoryId = quiz.getCategory().getCategoryId();

            // 총 문제 수
            long totalQuizCount = quizRepository.countByCategory_CategoryId(categoryId);

            // 기존 progress 조회 또는 새로 생성
            Progress progress = progressRepository
                    .findByMember_MemberIdAndCategory_CategoryId(memberId, categoryId)
                    .orElseGet(() -> Progress.builder()
                            .member(member)
                            .category(quiz.getCategory())
                            .progress(0)
                            .updateAt(LocalDateTime.now())
                            .build()
                    );

            // 증가 비율 = 1문제 / 전체문제 * 100
            int increase = (int) Math.round((1.0 / totalQuizCount) * 100.0);

            int newProgress = progress.getProgress() + increase;
            if (newProgress > 100) newProgress = 100;

            progress.setProgress(newProgress);
            progress.setUpdateAt(LocalDateTime.now());

            progressRepository.save(progress);
        }

        // ==========================================================
        // 4️⃣ 풀이 기록 저장
        // ==========================================================
        QuizHistory history = QuizHistory.builder()
                .member(member)
                .quiz(quiz)
                .gainExp(gainExp)
                .answerAt(LocalDateTime.now())
                .build();
        quizHistoryRepository.save(history);

        // ==========================================================
        // 5️⃣ 반환 데이터
        // ==========================================================
        Map<String, Object> data = new HashMap<>();
        data.put("isCorrect", isCorrect);
        data.put("gainExp", gainExp);

        return Map.of(
                "status", 200,
                "message", isCorrect ? "정답입니다! 포인트가 적립되었습니다." : "오답입니다.",
                "data", data
        );
    }



    //5. 퀴즈 기록 조회
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

    //6. 진행률 조회
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

    //7. 레벨 조회
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
