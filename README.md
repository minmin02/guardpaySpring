# 🛡️ GuardPay AI

### "어르신을 위한 디지털 금융 방패: 안심하고 배우는 금융 생활"

**GuardPay AI**는 복잡한 UI와 끊임없는 금융 사기 위협 속에서 어르신들이 소외되지 않도록 돕는 **금융 교육 플랫폼**입니다. 실제 송금 전, 안전한 샌드박스 환경에서 실습하고 AI와 대화하며 금융 역량을 키웁니다.

---

## 🎯 핵심 가치 (Core Values)

- **Risk-Free Learning**: 실제 자산 손실 걱정 없는 모의 송금 환경 제공
- **AI-Powered Guidance**: Google Gemini 기반의 맞춤형 프롬프트 엔지니어링을 통한 실시간 상담
- **User-Centric Design**: 금융 취약 계층의 인지 특성을 고려한 직관적인 사용자 경험

---

## ✨ 주요 기능 (Key Features)

### 1. AI 금융 상담 챗봇 (Gemini AI)

- **맞춤형 교육**: 사용자의 연령대와 관심사에 맞춘 금융 지식 전달
- **사기 예방**: 의심스러운 문자나 전화 내용을 입력하면 AI가 사기 위험도 분석 및 대응책 안내
- **대화형 UI**: 복잡한 메뉴 찾기 대신 대화를 통해 필요한 정보에 접근

### 2. 안심 모의 송금 훈련

- **실전 같은 가상 환경**: 계좌 이체 전 과정을 실제 앱처럼 구현한 훈련 시뮬레이터
- **랜덤 시나리오**: 다양한 송금 상황(경조사비, 손주 용돈 등) 제공으로 실응용력 향상
- **단계별 가이드**: 실수하기 쉬운 지점에서 시각적 가이드 및 경고 메시지 제공

### 3. 주변 은행 찾기 (Kakao Map)

- **위치 기반 검색**: 현재 내 위치에서 가장 가까운 은행 지점을 거리순으로 검색
- **지능형 Fallback**: 장소명 검색 실패 시 자동으로 주소 검색으로 전환하여 결과 누락 방지

### 4. 리워드 및 게이미피케이션

- **포인트 시스템**: 교육 영상 시청, 퀴즈 풀이, 송금 훈련 완료 시 포인트 지급
- **기프트샵**: 적립된 포인트를 실제 가치 있는 보상으로 교환하여 학습 동기 부여

---

## 🛠 기술 스택 (Technical Stack)

### Backend

- **Language**: Java 21
- **Framework**: Spring Boot 3.4
- **Security**: Spring Security, JWT (Stateless)
- **Data**: Spring Data JPA, MariaDB
- **API Communication**: Spring WebFlux (WebClient)

### Frontend

- **Framework**: Flutter 3.x
- **Language**: Dart

### AI & External API

- **AI Model**: Google Gemini 1.5 Flash (via Generative Language API)
- **Map Service**: Kakao Local API

---

## 🏗 시스템 아키텍처 및 설계 원칙

1. **관심사의 분리 (SoC)**: Controller(접점), Service(비즈니스 로직), Client(외부 통신) 계층을 엄격히 분리하여 Gemini나 Kakao API의 변경이 비즈니스 로직에 영향을 주지 않도록 설계했습니다.
2. **DTO 기반 타입 안정성**: `Map<String, Object>` 사용을 지양하고 Java Record 기반의 DTO를 도입하여 외부 API 응답의 타입 안정성을 확보했습니다.
3. **중앙 집중식 예외 처리**: `ApiResponse` 규격과 `GlobalExceptionHandler`를 통해 모든 API 응답 형식을 통일했습니다.

---

## 📚 API 명세 및 시작하기

### Swagger API Documentation

```java
서버 구동 후 아래 주소에서 인터랙티브한 API 명세를 확인할 수 있습니다.
http://localhost:8080/swagger-ui/index.html
```

### Backend Setup

Bash

```java
# 환경 변수 설정 (.env 파일 생성 필요)
# GEMINI_API_KEY, KAKAO_REST_API_KEY 등

./gradlew clean build
./gradlew bootRun
```
