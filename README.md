# 🌐 Sumte Server

## 1. 프로젝트 소개
Sumte Server는 **게스트하우스 예약·결제·리뷰·관리** 기능을 제공하는 백엔드 서버입니다.  

### Java Spring
- project build : Gradle 
- Spring Boot : 3.3.4  
- Java : 17  *#JDK 버전 통일*
- packaging : jar
- IDE : Intellij  
- 코드 컨벤션: 네이버 코드 컨벤션  
- Git branch 전략: Gutlab Flow 변형
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

### DevOps & Infra
- AWS EC2 / RDS / S3
- Nginx
- Docker
- GitHub Actions (CI/CD)
- VPC 환경 (Public / Private Subnet 분리)

## 2. 브랜치 전략
- **main** : 배포 버전
- **develop** : 개발 통합 브랜치
- **feature/** : 기능 개발 (예: `feature/reservation-api`)
- **hotfix/** : 긴급 수정
- **release/** : 배포 준비

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

