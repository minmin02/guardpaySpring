# 🛡️ GuardPay AI

> 금융 취약 계층을 위한 AI 기반 디지털 금융 교육 플랫폼
> 

## 📋 목차

- [프로젝트 소개](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%86%8C%EA%B0%9C)
- [주요 기능](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5)
- [기술 스택](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)
- [시스템 아키텍처](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)
- [시작하기](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0)
- [API 문서](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-api-%EB%AC%B8%EC%84%9C)
- [팀원 소개](https://claude.ai/chat/bd57cd05-8d74-49e6-8a75-be58c5bd4f7f#-%ED%8C%80%EC%9B%90-%EC%86%8C%EA%B0%9C)

## 🎯 프로젝트 소개

**GuardPay AI**는 금융 취약 계층(특히 어르신들)을 위한 디지털 금융 교육 플랫폼입니다.

### 추진 배경

복잡한 금융 앱과 증가하는 금융 사기로 어려움을 겪는 어르신들을 위해, **안심하고 금융 앱을 익힐 수 있는 맞춤형 교육 환경**을 제공합니다.

### 핵심 가치

- 🎓 **안전한 학습 환경**: 실제 금융 거래 없이 안전하게 연습할 수 있는 모의 송금 시스템
- 🤖 **AI 맞춤형 지원**: Google Gemini 기반 금융 상담 챗봇으로 실시간 질의응답
- 🎮 **게이미피케이션**: 퀴즈와 보상 시스템으로 학습 동기 부여
- 📺 **다양한 학습 콘텐츠**: 금융 교육 영상 및 단계별 학습 프로그램

## ✨ 주요 기능

### 1. 회원 관리 및 인증

- ✅ 이메일/비밀번호 기반 회원가입 및 로그인
- ✅ 소셜 로그인 (Google, Kakao)
- ✅ JWT 기반 인증 (Access Token / Refresh Token)
- ✅ SMTP를 통한 비밀번호 재설정

### 2. 금융 교육 콘텐츠

- 📹 금융 교육 영상 라이브러리
- 📝 금융 지식 퀴즈
- 🎯 개인 맞춤형 학습 추천

### 3. 모의 송금 훈련

- 💳 가상 계좌를 통한 안전한 송금 연습
- 🎭 랜덤 송금 대상자 생성
- ✅ 단계별 송금 프로세스 학습
- 🏆 훈련 완료 시 포인트 지급

### 4. AI 금융 상담 챗봇

- 🤖 Google Gemini API 기반 대화형 챗봇
- 💬 금융 지식 및 사기 예방 상담
- 📊 개인 맞춤형 금융 조언

### 5. 리워드 시스템

- 🎁 학습 활동을 통한 포인트 적립
- 🛍️ 포인트 기프트샵
- 📈 개인 역량 진단 및 레벨 시스템

## 🛠 기술 스택

### Frontend

```
Flutter 3.x
Dart

```

### Backend

```
Spring Boot 3.5.5
Java 21
Spring Security (JWT 인증)
Spring Data JPA
Spring AI (OpenAI Integration)

```

### Database

```
MariaDB (Production)
H2 Database (Development)

```

### AI/ML

```
Google Gemini API

```

### Infrastructure & DevOps

```
GitHub
GitHub Actions (CI/CD)
Swagger/OpenAPI 3.0 (API Documentation)

```

### Collaboration Tools

```
Notion (문서화)
Figma (디자인)

```

## 🏗 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                         Frontend                            │
│                      Flutter / Dart                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                         Backend                             │
│                   Spring Boot / Java                        │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │   Security   │  │   Data JPA   │  │   REST API      │  │
│  │   (JWT)      │  │              │  │   (Controllers) │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
└──────────────────────┬──────────────────┬───────────────────┘
                       │                  │
                       ▼                  ▼
         ┌─────────────────────┐  ┌──────────────────┐
         │   Database          │  │   AI / LLM       │
         │   MariaDB / H2      │  │   Google Gemini  │
         └─────────────────────┘  └──────────────────┘

```

## 🚀 시작하기

### 필수 요구사항

- Java 21+
- Gradle 7.x+
- MariaDB 10.x+ (또는 H2 for development)
- Flutter 3.x+

### Backend 설정

1. **환경 변수 설정** (`application.yml`)

```yaml
spring:
  application:
    name: guardpay

  profiles:
    active: local   # ?? ?? ????: local

---
jwt:
  # ⬇️ 이 부분이 가장 중요합니다. 외부에 노출되면 안 되는 매우 긴 비밀 키를 입력하세요.
  secret-key: V293ISB0aGlzIGlzIGEgdmVyeSBsb25nIGFuZCBzZWN1cmUgc2VjcmV0IGtleSBmb3IgSldUIGVuY29kaW5nLiBEbyBub3Qgc2hhcmUgaXQgd2l0aCBhbnlvbmUh
  access-token-validity-in-seconds: 3600   # 1시간
  refresh-token-validity-in-seconds: 604800 # 7일

spring:
  config:
    activate:
      on-profile: local

  ai:
    openai:
      api-key: dummy   # open ai ??

  datasource:
    url: jdbc:h2:mem:guardpay
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create   # ??? ??? ??? ?? ??
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080
  address: 0.0.0.0

---

spring:
  config:
    activate:
      on-profile: dev

  datasource:
    url: jdbc:mysql://localhost:3306/guardpay?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: root
    password:

  jpa:
    hibernate:
      ddl-auto: update   # ?? DB?? update ?? ??
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

---

spring:
  config:
    activate:
      on-profile: prod

  datasource:
    url: jdbc:mysql://prod-db-server:3306/guardpay?useSSL=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: prod_user
    password: prod_password
  jpa:
    hibernate:
      ddl-auto: validate   # ?? DB? ??? ?? ?? ??
    show-sql: false

```

1. **환경 변수 설정** (`application-local.yml`)

```yaml
spring:
  datasource:
    # 1. URL의 'mysql'을 'mariadb'로 변경
    url: jdbc:mariadb://localhost:3306/guardpay?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    # 2. MariaDB 드라이버 클래스 이름으로 변경
    driver-class-name: org.mariadb.jdbc.Driver
    username: root
    password: 1234 # 여기에 실제 비밀번호를 입력하세요

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    # 3. Hibernate Dialect(방언)를 MariaDB에 맞게 변경
    database-platform: org.hibernate.dialect.MariaDBDialect
    defer-datasource-initialization: true

  sql:
    init:
      mode: always

  jwt:
    # ⬇️ 이 부분이 가장 중요합니다. 외부에 노출되면 안 되는 매우 긴 비밀 키를 입력하세요.
    secret-key: V293ISB0aGlzIGlzIGEgdmVyeSBsb25nIGFuZCBzZWN1cmUgc2VjcmV0IGtleSBmb3IgSldUIGVuY29kaW5nLiBEbyBub3Qgc2hhcmUgaXQgd2l0aCBhbnlvbmUh
    access-token-validity-in-seconds: 3600   # 1시간
    refresh-token-validity-in-seconds: 604800 # 7일

  # ✅ [추가] OAuth2 클라이언트 설정
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: 807215747095-niibg754e2kv5575hp74o0e6eov1h4eq.apps.googleusercontent.com
            client-secret: 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
            scope:
              - profile
              - email
            redirect-uri: "https://progestational-lurlene-supportable.ngrok-free.dev/login/oauth2/code/google"

  mail:
    host: smtp.gmail.com
    port: 587
    # 1. 2단계 인증 설정한 본인의 Google 이메일
    username: aktr378@gmail.com
    # 2. 발급받은 16자리 '앱 비밀번호' (공백 없이)
    password: irbrtqnrepiqgzkh
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true

springdoc:
  api-docs:
    path: /v3/api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    display-request-duration: true
    groups-order: DESC
  default-consumes-media-type: application/json
  default-produces-media-type: application/json

# ⬇️ [신규 추가] Gemini API 설정
gemini:
  api:
    key: AIzaSyAlsxM6Bwb1QAM78nVd516m1vYBoQbTFbw
    model: gemini-2.5-flash
  prompts:
    financial-education:
      system: classpath:prompts/financial-education-system.txt
      user: classpath:prompts/financial-education-user.txt

google:
  client-id: 807215747095-niibg754e2kv5575hp74o0e6eov1h4eq.apps.googleusercontent.com
  client-secret: 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
  scope:
    - profile
    - email
  redirect-uri: "https://progestational-lurlene-supportable.ngrok-free.dev/login/oauth2/code/google"
```

1. **프로젝트 빌드 및 실행**

```bash
./gradlew clean build
./gradlew bootRun

```

서버가 `http://localhost:8080`에서 실행됩니다.

### Frontend 설정

```bash
cd flutter_app
flutter pub get
flutter run
```

## 📚 API 문서

API 문서는 Swagger UI를 통해 확인할 수 있습니다.

서버 실행 후 다음 URL에 접속하세요:

```
http://localhost:8080/swagger-ui/index.html

```

### 주요 API 엔드포인트

### 인증 API

- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/kakao` - 카카오 로그인
- `POST /api/auth/google` - 구글 로그인
- `POST /api/auth/reissue` - 토큰 재발급
- `POST /api/auth/password-reset-request` - 비밀번호 재설정
- `GET /api/auth/check-email` - 이메일 중복 확인

### 챗봇 API

- `POST /api/chat/financial-advice` - 금융 상담 챗봇

<br/>

## 📅 개발 일정

## 👥 팀원 소개

| 이름 | 학번 | 역할 | GitHub |
| --- | --- | --- | --- |
| 김민규 | 2171263 | 팀장, Backend 개발 | [@minmin](https://github.com/minmin02?tab=repositories) |
| 김지원 | 2371153 | Backend 개발 | [@username](https://github.com/username) |
| 이준호 | 2371083 | Frontend 개발 | [@username](https://github.com/username) |
| 박초은 | 2271112 | Frontend 개발 | [@username](https://github.com/username) |

## 📝 요구사항 요약

### 기능 요구사항 (SFR)

- 총 **20개**의 기능 요구사항 정의
- 로그인/회원가입, 소셜 로그인, SMTP 이메일 인증
- 모의 송금 훈련, AI 챗봇, 영상 학습
- 퀴즈 시스템, 포인트 및 리워드 시스템

### 비기능 요구사항

- **성능 요구사항 (PER)**: 1개
- **인터페이스 요구사항 (INR)**: 1개

## 🎯 기대 효과

1. **디지털 금융 자립 지원**
    - 안전한 실습 환경으로 금융 취약 계층의 디지털 금융 자립 지원
2. **금융 사기 예방**
    - AI 챗봇을 통한 실시간 사기 예방 교육
3. **금융 안전 습관 형성**
    - 게이미피케이션과 보상 시스템으로 지속적인 학습 동기 부여
4. **접근성 향상**
    - 모바일 기반 플랫폼으로 언제 어디서나 학습 가능

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](https://claude.ai/chat/LICENSE) file for details.

## 🔗 관련 링크

- [Notion 프로젝트 문서](https://www.notion.so/GuardPay-26a5063af16f80a49125d8ccbbaf1fa5?pvs=21)
- [Figma 디자인](https://www.figma.com/design/8PL3hbLyUHjThwu9b4dk2j/%EA%B3%A0%EB%AA%A8%ED%94%84---GuardPay?node-id=0-1&p=f&t=Rm96NH6NbbiUj5DS-0)
- [API 명세서](http://localhost:8080/swagger-ui/index.html)

## 📧 문의

프로젝트에 대한 문의사항이 있으시면 아래로 연락주세요:

- 이메일: aktr378@gmail.com
- 이슈 트래커: [GitHub Issues](https://github.com/your-username/guardpay/issues)

---

**Made with ❤️ by GuardPay Team**
