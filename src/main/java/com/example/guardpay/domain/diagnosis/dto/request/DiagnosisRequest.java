package com.example.guardpay.domain.diagnosis.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DiagnosisRequest {

    private List<AnswerDto> answers;

    @Getter
    @Setter
    public static class AnswerDto {
        @JsonProperty("quizID")
        private Long quizID;          // 또는 questionId — JSON 키에 맞게
        @JsonProperty("selectedAnswer")
        private Long selectedAnswer;  // 선택한 보기 ID

        @Override
        public String toString() {
            return "AnswerDto{" +
                    "quizId=" + quizID +
                    ", selectedAnswer=" + selectedAnswer +
                    '}';
        }
    }


}
