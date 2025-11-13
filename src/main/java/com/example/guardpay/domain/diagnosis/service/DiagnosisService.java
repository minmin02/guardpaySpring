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

    public Map<String, Object> getDiagnosisQuestions() {
        // ✅ 카테고리별 정보 (이름도 포함)
        Map<Long, String> categoryNames = Map.of(
                1L, "금융 기본 지식",
                2L, "사기 예방",
                3L, "예적금 개념"
        );

        List<Map<String, Object>> parts = new ArrayList<>();

        // ✅ 카테고리 1~3까지 반복
        for (Map.Entry<Long, String> entry : categoryNames.entrySet()) {
            Long categoryId = entry.getKey();
            String categoryName = entry.getValue();

            List<Map<String, Object>> questions = new ArrayList<>();

            // ✅ 각 카테고리에서 level 1~3 한 문제씩 뽑기
            for (int level = 1; level <= 3; level++) {
                List<Quiz> quizList = quizRepository.findByCategoryIdAndLevel(categoryId, level);

                if (!quizList.isEmpty()) {
                    // ✅ 랜덤으로 한 문제 선택
                    Quiz quiz = quizList.get(new Random().nextInt(quizList.size()));

                    // ✅ 보기 구성
                    Map<String, String> options = new LinkedHashMap<>();
                    List<QuizOption> quizOptions = quiz.getOptions();
                    for (int i = 0; i < quizOptions.size(); i++) {
                        options.put(String.valueOf(i + 1), quizOptions.get(i).getOptionText());
                    }

                    Map<String, Object> questionMap = Map.of(
                            "questionId", quiz.getQuizId(),
                            "questionText", quiz.getQuestion(),
                            "options", options
                    );
                    questions.add(questionMap);
                }
            }

            Map<String, Object> part = Map.of(
                    "partId", categoryId,
                    "partName", categoryName,
                    "questions", questions
            );
            parts.add(part);
        }

        // ✅ 최종 응답 반환
        return Map.of(
                "status", 200,
                "message", "진단 문제 조회가 완료되었습니다.",
                "data", Map.of("parts", parts)
        );
    }


    public Map<String, Object> submitDiagnosis(String jwtToken, List<DiagnosisRequest.AnswerDto> answers) {
        System.out.println("🧩 제출된 답변들: " + answers);
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
        System.out.println(Map.of(
                "status", 201,
                "message", "진단 결과가 성공적으로 제출되었습니다.",
                "data", Map.of(
                        "historyId", history.getHistoryId(),
                        "finalGrade", finalGrade)
        ));
        return Map.of(
                "status", 201,
                "message", "진단 결과가 성공적으로 제출되었습니다.",
                "data", Map.of(
                        "historyId", history.getHistoryId(),
                        "finalGrade", finalGrade)
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
