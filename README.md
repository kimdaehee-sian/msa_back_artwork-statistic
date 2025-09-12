# Artwork Statistics Service

### 서비스명: artwork-statistics
**역할**: 작품 좋아요 관리 및 통계 제공  
**포트**: 8080 (기본값)

---

## 주요 기능

### 좋아요 관리
- `POST /api/likes` – 작품 좋아요 저장

### 통계 조회
- `GET /api/v1/artwork-stats/top3` – 상위 3개 인기 작품 조회
- `GET /api/v1/artwork-stats/test` – 서비스 상태 확인

### 헬스 체크
- `GET /api/health` – 애플리케이션 상태 확인
- `GET /actuator/health` – Spring Actuator 헬스 체크

---

## 실행 방법

### 로컬 개발 환경
```bash
./gradlew bootRun
```

### Docker 실행
```bash
docker build -t artwork-statistics .
docker run -p 8080:8080 artwork-statistics
```

### 프로필별 실행
```bash
# 최소 설정 (H2 DB)
./gradlew bootRun --args='--spring.profiles.active=minimal'

# Azure 환경
./gradlew bootRun --args='--spring.profiles.active=azure'
```

---

## API 테스트

### Swagger UI
- **로컬**: http://localhost:8080/swagger-ui.html
- **Azure**: https://guidely-artwork-statistic-fah0b3dte6hvech2.koreacentral-01.azurewebsites.net/swagger-ui.html

### API 예시

#### 작품 좋아요 저장
```bash
curl -X POST "http://localhost:8080/api/likes" \
  -H "Content-Type: application/json" \
  -d '{
    "artworkId": 1,
    "userId": "user123"
  }'
```

#### 상위 3개 작품 조회
```bash
curl -X GET "http://localhost:8080/api/v1/artwork-stats/top3"
```

**응답 예시:**
```json
{
  "topArtworks": [
    {
      "artworkId": 1,
      "name": "용과 호랑이",
      "artist": "미상",
      "imageUrl": "http://www.museum.go.kr/uploadfile/...",
      "likeCount": 4
    }
  ]
}
```

---

## 데이터베이스 정보

### 지원 데이터베이스
- **Production**: MySQL 8.0+
- **Development**: H2 (인메모리)

### 테이블 구조

#### likes 테이블
```sql
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artwork_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL
);
```

### Entity 구조
- **Like**: 작품 좋아요 정보
  - `id`: 좋아요 고유 ID
  - `artworkId`: 작품 ID (외부 서비스 참조)
  - `createdAt`: 좋아요 생성 시간

---

## 환경 설정

### 환경 변수

#### 데이터베이스 설정
- `AZURE_MYSQL_HOST`: MySQL 호스트
- `AZURE_MYSQL_PORT`: MySQL 포트 (기본값: 3306)
- `AZURE_MYSQL_DATABASE`: 데이터베이스 명 (기본값: artwork_statistics)
- `AZURE_MYSQL_USERNAME`: 데이터베이스 사용자명
- `AZURE_MYSQL_PASSWORD`: 데이터베이스 비밀번호

#### 외부 서비스 설정
- `EXHIBITION_SERVICE_URL`: 전시 작품 서비스 URL
- `SERVER_PORT`: 애플리케이션 포트 (기본값: 8080)

### 프로필 설정
- **default**: MySQL 사용 (Production)
- **minimal**: H2 인메모리 DB 사용 (Development)
- **azure**: Azure 환경 설정

---

## 외부 서비스 연동

### Exhibition Artwork Service
- **목적**: 작품 상세 정보 조회
- **URL**: https://guidely-exhibition-artwork-services-dmeagqebfud4e7hh.koreacentral-01.azurewebsites.net
- **연동 API**: `GET /api/artworks/{artworkId}`

---

## 기술 스택

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0 / H2
- **ORM**: Spring Data JPA (Hibernate)
- **Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Build Tool**: Gradle
- **Container**: Docker

---

## 배포 정보

### Azure App Service
- **URL**: https://guidely-artwork-statistic-fah0b3dte6hvech2.koreacentral-01.azurewebsites.net
- **환경**: Production
- **프로필**: default (MySQL 사용)

---

## 담당자

**개발자**: [담당자 이름]  
**담당 기능**: 
- 작품 좋아요 시스템 구현
- 작품 인기도 통계 API 개발
- 외부 서비스 연동 (Exhibition Service)

---

## 라이선스

이 프로젝트는 Guidely 팀의 MSA 프로젝트의 일부입니다.
