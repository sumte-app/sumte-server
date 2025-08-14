# 🌐 Sumte Server


## 1. 프로젝트 소개
<img width="120" height="120" alt="sumte1" src="https://github.com/user-attachments/assets/7a33872c-a376-4d23-86d1-5ae7b16cacf8" />

**숨터**는 나에게 꼭 맞는 게스트하우스를 찾아주는 숙박 서비스입니다.
제주 전역의 게스트하우스를 검색하고, 위치·시설·이벤트(파티,조식 등)까지 세부 필터링이 가능합니다.
단순 숙소 예약을 넘어, 머무는 동안 더 깊이 있는 경험과 인연을 만들 수 있도록 돕습니다.  

  
팀장: 조용혁  
팀원: 이서희, 백준규, 장효원

### Java Spring
- project build : Gradle 
- Spring Boot : 3.3.4  
- Java : 17
- packaging : jar
- IDE : Intellij  
- 코드 컨벤션: 네이버 코드 컨벤션  
- 패키지 전략: 도메인 패키지 전략

### 데이터베이스
- MySQL

### 라이브러리
- Spring Web
- Spring Data JPA
- MySQL Driver
- Lombok
- Spring Boot Devtools
- Springdoc
- Spring Security
- QueryDSL
- JWT
- Swagger
- AWS S3
  
### 서버 아키텍처 다이어그램
<img width="1920" height="1080" alt="UMC_숨터_백엔드-007" src="https://github.com/user-attachments/assets/ff44c0d0-4636-4594-bb3f-d3de6bf26505" />

### DevOps & Infra
- AWS EC2 / RDS / S3
- Nginx
- Docker
- GitHub Actions (CI/CD)
- VPC 환경 (Public / Private Subnet 분리)

## 2. 브랜치 전략
Gutlab Flow 변형 (main[배포] - develop - feature)
- **main** : 배포 버전
- **develop** : 개발 통합 브랜치
- **feature/** : 기능 개발 (예: `feature/reservation-api`)

---

## 3. 프로젝트 구조
```plaintext
📦 src
 ┣ 📂 main
 ┃ ┣ 📂 java/com/sumte
 ┃ ┃ ┣ 📂 apiPayload     # API 응답 코드, 예외 처리
 ┃ ┃ ┣ 📂 config         # 설정 관련
 ┃ ┃ ┣ 📂 guesthouse     # 게스트하우스 도메인
 ┃ ┃ ┣ 📂 image          # 이미지 업로드/관리
 ┃ ┃ ┣ 📂 jpa            # JPA 설정
 ┃ ┃ ┣ 📂 payment        # 결제 도메인
 ┃ ┃ ┣ 📂 reservation    # 예약 도메인
 ┃ ┃ ┣ 📂 review         # 리뷰 도메인
 ┃ ┃ ┣ 📂 room           # 객실 도메인
 ┃ ┃ ┣ 📂 security       # 인증/인가
 ┃ ┃ ┣ 📂 user           # 사용자 관리
 ┃ ┃ ┣ HealthHeckController
 ┃ ┃ ┗ SumteApplication
 ┃ ┗ 📂 resources        # 설정 파일, 정적 리소스

