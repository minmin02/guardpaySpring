package com.example.guardpay.domain.diagnosis.service;

import com.example.guardpay.domain.diagnosis.dto.request.DiagnosisRequest;
import com.example.guardpay.domain.diagnosis.entity.DiagnosisHistory;
import com.example.guardpay.domain.diagnosis.repository.DiagnosisHistoryRepository;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.entity.*;
import com.example.guardpay.domain.quiz.repository.*;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final QuizRepository quizRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final MemberRepository memberRepository;
    private final MemberLevelRepository memberLevelRepository;
    private final DiagnosisHistoryRepository diagnosisHistoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, Object> submitDiagnosis(String jwtToken, List<DiagnosisRequest.AnswerDto> answers) {
        // ✅ JWT 토큰 검증 및 유저 추출
        if (!jwtTokenProvider.validateToken(jwtToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
        Long memberId = jwtTokenProvider.getMemberId(jwtToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        int correctCount = 0;
        Map<Long, Integer> categoryHighestLevel = new HashMap<>();

        // ✅ 정답 채점
        for (DiagnosisRequest.AnswerDto answer : answers) {
            System.out.println("🧩 요청으로 들어온 데이터: " + answer);
            if (answer.getQuizID() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "퀴즈 ID가 누락되었습니다.");
            }
            if (answer.getSelectedAnswer() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "선택한 보기가 누락되었습니다.");
            }

            Long quizId = answer.getQuizID();
            Long selectedAnswerId = answer.getSelectedAnswer();

            Quiz quiz = quizRepository.findById(quizId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 퀴즈입니다."));
            QuizOption selected = quizOptionRepository.findById(selectedAnswerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 보기입니다."));

            if (Boolean.TRUE.equals(selected.getIsCorrect())) {
                correctCount++;

                Long categoryId = quiz.getCategory().getCategoryId();
                int quizLevel = quiz.getLevel();

                categoryHighestLevel.merge(categoryId, quizLevel, Math::max);
            }
        }

        // ✅ 점수 계산 및 등급 부여
        int score = correctCount * 10;
        String finalGrade = getGrade(score);

        // ✅ 각 카테고리별 최고 난이도를 MemberLevel에 반영
        categoryHighestLevel.forEach((categoryId, level) -> {
            MemberLevel ml = MemberLevel.builder()
                    .member(member)
                    .category(Category.builder().categoryId(categoryId).build())
                    .level(level)
                    .updateAt(LocalDateTime.now())
                    .build();
            memberLevelRepository.save(ml);
        });

        // ✅ 결과 저장
        DiagnosisHistory history = DiagnosisHistory.builder()
                .member(member)
                .score(score)
                .finalGrade(finalGrade)
                .createdAt(LocalDateTime.now())
                .build();

        diagnosisHistoryRepository.save(history);

        return Map.of(
                "status", 201,
                "message", "진단 결과가 성공적으로 제출되었습니다.",
                "data", Map.of("historyId", history.getHistoryId())
        );
    }

    public Map<String, Object> getDiagnosisHistory(Long historyId) {
        DiagnosisHistory history = diagnosisHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 진단 결과입니다."));

        Map<String, Object> data = Map.of(
                "historyId", history.getHistoryId(),
                "score", history.getScore(),
                "finalGrade", history.getFinalGrade()
        );

        return Map.of(
                "status", 200,
                "message", "진단 결과 조회 성공",
                "data", data
        );
    }

    // ✅ 등급 계산 로직
    private String getGrade(int score) {
        if (score >= 90) return "안전 송금 마스터";
        if (score >= 70) return "금융 방패단";
        if (score >= 50) return "초보 금융가";
        return "주의 필요";
    }
}
