package com.example.guardpay.domain.diagnosis.service;

import com.example.guardpay.domain.diagnosis.converter.DiagnosisConverter;
import com.example.guardpay.domain.diagnosis.data.DiagnosisGrade;
import com.example.guardpay.domain.diagnosis.dto.req.DiagnosisRequest;
import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisHistoryResponse;
import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisPartResponse;
import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisQuestionDto;
import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisResponse;
import com.example.guardpay.domain.diagnosis.entity.DiagnosisHistory;
import com.example.guardpay.domain.diagnosis.enums.DiagnosisErrorCode;
import com.example.guardpay.domain.diagnosis.exception.DiagnosisException;
import com.example.guardpay.domain.diagnosis.repository.DiagnosisHistoryRepository;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.domain.quiz.entity.*;
import com.example.guardpay.domain.quiz.enums.QuizErrorCode;
import com.example.guardpay.domain.quiz.exception.QuizException;
import com.example.guardpay.domain.quiz.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final QuizRepository quizRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final MemberRepository memberRepository;
    private final MemberLevelRepository memberLevelRepository;
    private final DiagnosisHistoryRepository diagnosisHistoryRepository;

    public List<DiagnosisPartResponse> getDiagnosisQuestions() {

        Map<Long, String> categoryNames = Map.of(
                1L, "금융 기본 지식",
                2L, "사기 예방",
                3L, "예적금 개념"
        );

        return categoryNames.entrySet().stream()
                .map(entry -> {
                    Long categoryId = entry.getKey();
                    String categoryName = entry.getValue();

                    // 레벨 1~3에서 각각 랜덤하게 한 문제씩 추출
                    List<DiagnosisQuestionDto> questions = IntStream.rangeClosed(1, 3)
                            .mapToObj(level -> quizRepository.findByCategoryIdAndLevel(categoryId, level))
                            .filter(list -> !list.isEmpty())
                            .map(list -> list.get(new Random().nextInt(list.size())))
                            .map(DiagnosisConverter::toDiagnosisQuestionDto)
                            .toList();

                    return new DiagnosisPartResponse(categoryId, categoryName, questions);
                })
                .toList();


    }





    @Transactional
    public DiagnosisResponse submitDiagnosis(Long memberId, DiagnosisRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new DiagnosisException(DiagnosisErrorCode.INVALID_ANSWERS);
        }

        int totalScore = 0;

        // 점수 계산
        for (DiagnosisRequest.AnswerDto answer : request.getAnswers()) {
            Quiz quiz = quizRepository.findById(answer.getQuizID())
                    .orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));

            QuizOption selected = quizOptionRepository.findById(answer.getSelectedAnswer())
                    .orElseThrow(() -> new QuizException(QuizErrorCode.OPTION_NOT_FOUND));

            if (selected.getIsCorrect()) {
                totalScore += quiz.getPoint();
            }
        }

        // 등급 결정 및 결과 생성
        DiagnosisGrade grade = DiagnosisGrade.fromScore(totalScore);
        return new DiagnosisResponse(
                grade.getDescription(),
                totalScore,
                "역량 진단이 성공적으로 완료되었습니다."
        );
    }



    @Transactional(readOnly = true)
    public DiagnosisHistoryResponse getDiagnosisHistory(Long historyId) {
        DiagnosisHistory history = diagnosisHistoryRepository.findById(historyId)
                .orElseThrow(() -> new QuizException(QuizErrorCode.OPTION_NOT_FOUND));

        return new DiagnosisHistoryResponse(
                history.getHistoryId(),
                history.getScore(),
                history.getFinalGrade()
        );
    }


}
