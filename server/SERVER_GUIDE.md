# Reborn 서버 가이드

이 문서는 서버 모듈의 구조와 동작 원리를 초보 개발자도 이해할 수 있도록 설명합니다.

---

## 1. 서버란?

앱(클라이언트)이 "업체 정보 보여줘"라고 요청하면, **서버**가 데이터베이스에서 정보를 꺼내 JSON 형태로 응답합니다.

```
[앱] ──요청──▶ [서버] ──조회──▶ [데이터베이스(PostgreSQL)]
[앱] ◀──응답── [서버] ◀──결과── [데이터베이스(PostgreSQL)]
```

---

## 2. 사용하는 기술 스택

| 기술 | 역할 | 비유 |
|------|------|------|
| **Spring Boot** | 서버 프레임워크 | 식당의 건물 자체 |
| **Exposed** | ORM (DB 쿼리를 Kotlin 코드로 작성) | 주방장에게 주문을 전달하는 웨이터 |
| **PostgreSQL** | 데이터베이스 | 재료를 보관하는 냉장고 |
| **Kotlin Serialization** | 데이터 ↔ JSON 변환 | 주문서 양식 |

---

## 3. 프로젝트 구조

```
server/
├── build.gradle.kts                    ← 의존성(라이브러리) 설정
├── src/main/resources/
│   └── application.yml                 ← DB 접속 정보, 포트 설정
└── src/main/kotlin/com/hyunjine/reborn/
    ├── RebornApplication.kt            ← 서버 시작점 (main 함수)
    ├── config/
    │   ├── WebConfig.kt                ← JSON 직렬화 설정
    │   ├── ExposedConfig.kt            ← DB 테이블 자동 생성
    │   └── DatabaseInitializer.kt      ← 샘플 데이터 삽입
    └── store/
        ├── table/
        │   └── StoreTables.kt          ← DB 테이블 정의
        ├── StoreRepository.kt          ← DB 조회 로직
        └── StoreController.kt          ← API 엔드포인트
```

---

## 4. 서버 실행 전 준비사항

### 4-1. PostgreSQL 설치

[PostgreSQL 공식 사이트](https://www.postgresql.org/download/)에서 설치합니다.

### 4-2. 데이터베이스 생성

설치 후 터미널(또는 pgAdmin)에서 다음 SQL을 실행합니다:

```sql
-- 데이터베이스 생성
CREATE DATABASE reborn;

-- 사용자 생성 및 비밀번호 설정
CREATE USER reborn WITH PASSWORD 'reborn';

-- 권한 부여
GRANT ALL PRIVILEGES ON DATABASE reborn TO reborn;

-- PostgreSQL 15 이상에서는 스키마 권한도 필요합니다
\c reborn
GRANT ALL ON SCHEMA public TO reborn;
```

### 4-3. 서버 실행

```bash
./gradlew :server:bootRun
```

서버가 시작되면 자동으로:
1. 테이블이 생성되고 (`ExposedConfig`)
2. 샘플 데이터가 삽입됩니다 (`DatabaseInitializer`)

### 4-4. 동작 확인

브라우저 또는 터미널에서 확인합니다:

```bash
curl http://localhost:8080/api/stores/1
```

---

## 5. 코드 상세 설명

### 5-1. application.yml - 설정 파일

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reborn   # DB 주소
    username: reborn                                # DB 사용자
    password: reborn                                # DB 비밀번호
  exposed:
    generate-ddl: true                              # 테이블 자동 생성 활성화

server:
  port: 8080                                        # 서버 포트
```

> **`jdbc:postgresql://localhost:5432/reborn`의 의미:**
> - `localhost` → 내 컴퓨터
> - `5432` → PostgreSQL 기본 포트
> - `reborn` → 데이터베이스 이름

---

### 5-2. StoreTables.kt - 테이블 정의

DB 테이블을 Kotlin 코드로 정의합니다. SQL의 `CREATE TABLE`과 같은 역할입니다.

```kotlin
// SQL로 쓰면 이렇게 됩니다:
// CREATE TABLE stores (
//     id    BIGSERIAL PRIMARY KEY,
//     name  VARCHAR(100),
//     ...
// );

// Exposed로 쓰면 이렇게 됩니다:
object Stores : LongIdTable("stores") {
    val name = varchar("name", 100)
    val address = varchar("address", 255)
    val description = text("description")
    val phoneNumber = varchar("phone_number", 20)
    val lastUpdated = datetime("last_updated")
}
```

**테이블 관계도:**

```
Stores (업체)
  │
  ├── 1:N ── StoreImages (업체 이미지)
  │           한 업체에 여러 이미지
  │
  ├── 1:N ── StoreBusinessHours (영업시간)
  │           한 업체에 요일별 7개 행
  │
  └── 1:N ── StorePrices (매입 시세)
              한 업체에 여러 품목
```

> **1:N 관계란?**
> 하나의 업체(1)에 여러 이미지(N)가 연결되는 것입니다.
> `StoreImages` 테이블의 `storeId` 컬럼이 `Stores`의 `id`를 참조(외래키)합니다.

---

### 5-3. StoreRepository.kt - DB 조회 로직

DB에서 데이터를 꺼내 모델 객체로 변환하는 계층입니다.

```kotlin
// SQL로 쓰면:
// SELECT * FROM stores WHERE id = ?;

// Exposed로 쓰면:
Stores.selectAll()
    .where { Stores.id eq id }
    .singleOrNull()
```

**조회 흐름:**

```
findStoreDetailById(id = 1)
  │
  ├── 1) Stores 테이블에서 기본 정보 조회
  │      → 이름, 주소, 설명, 전화번호, 최종 업데이트
  │
  ├── 2) StoreImages 테이블에서 이미지 URL 목록 조회
  │      → ["url1", "url2", "url3", "url4"]
  │
  ├── 3) StoreBusinessHours 테이블에서 영업시간 조회
  │      → 월~일 요일별 영업 여부 + 시간
  │
  ├── 4) StorePrices 테이블에서 시세 조회
  │      → [("고철", "450원/kg"), ...]
  │
  └── 5) 모든 데이터를 StoreDetailModel로 조합하여 반환
```

> **`@Transactional(readOnly = true)`란?**
> 이 함수가 실행되는 동안 하나의 DB 트랜잭션으로 묶인다는 뜻입니다.
> `readOnly = true`는 "읽기만 할 거야"라고 DB에 알려주어 성능을 최적화합니다.

---

### 5-4. StoreController.kt - API 엔드포인트

앱이 호출하는 URL을 정의하는 계층입니다.

```kotlin
@RestController                    // 이 클래스는 REST API를 제공합니다
@RequestMapping("/api/stores")     // 기본 경로: /api/stores
class StoreController(
    private val storeRepository: StoreRepository  // Spring이 자동으로 주입
) {
    @GetMapping("/{id}")           // GET /api/stores/{id}
    fun getStoreDetail(@PathVariable id: Long): ResponseEntity<StoreDetailModel>
}
```

**응답 예시:**

| 상황 | HTTP 상태 | 응답 |
|------|----------|------|
| 업체가 존재함 | `200 OK` | JSON 데이터 |
| 업체가 없음 | `404 Not Found` | 빈 응답 |

---

### 5-5. 서버 시작 시 실행 순서

```
서버 시작 (bootRun)
  │
  ├── 1) Spring Boot 초기화
  │      └── 컴포넌트 스캔, 빈 등록
  │
  ├── 2) WebConfig 적용
  │      └── JSON 직렬화 설정 (ImmutableList 지원)
  │
  ├── 3) ExposedConfig 실행 (@Order 1)
  │      └── 테이블이 없으면 자동 생성
  │
  ├── 4) DatabaseInitializer 실행 (@Order 2)
  │      └── 데이터가 없으면 샘플 데이터 삽입
  │
  └── 5) 서버 준비 완료 (포트 8080)
         └── API 요청 수신 대기
```

> **`@Order`란?**
> 여러 `ApplicationRunner`가 있을 때 실행 순서를 지정합니다.
> 숫자가 작을수록 먼저 실행됩니다. 테이블을 먼저 만들어야(Order 1)
> 데이터를 넣을 수 있으니까(Order 2) 이 순서가 중요합니다.

---

### 5-6. 전체 데이터 흐름 (요청 → 응답)

```
앱에서 GET /api/stores/1 요청
        │
        ▼
StoreController.getStoreDetail(id = 1)
        │
        ▼
StoreRepository.findStoreDetailById(id = 1)
        │
        ├── SELECT * FROM stores WHERE id = 1
        ├── SELECT * FROM store_images WHERE store_id = 1
        ├── SELECT * FROM store_business_hours WHERE store_id = 1
        └── SELECT * FROM store_prices WHERE store_id = 1
        │
        ▼
StoreDetailModel 객체 생성 (shared 모듈에 정의된 모델)
        │
        ▼
WebConfig의 Kotlin Serialization이 JSON으로 변환
        │
        ▼
앱에서 JSON을 받아 같은 StoreDetailModel로 역직렬화
```

> **핵심 포인트:** 서버와 앱이 `shared` 모듈의 **동일한 모델 클래스**를 사용하므로
> 데이터 구조가 항상 일치합니다. 서버에서 필드를 바꾸면 앱에서도 자동으로 반영됩니다.

---

## 6. 자주 하는 작업

### 새 API 추가하기

1. **테이블 정의** (`StoreTables.kt`에 추가하거나 새 파일 생성)
2. **Repository 메서드 추가** (DB 조회/저장 로직)
3. **Controller 엔드포인트 추가** (URL 매핑)
4. **ExposedConfig에 새 테이블 등록** (자동 생성 대상에 추가)

### DB 데이터 직접 확인하기

```bash
# PostgreSQL 접속
psql -U reborn -d reborn

# 업체 목록 조회
SELECT * FROM stores;

# 특정 업체의 시세 조회
SELECT * FROM store_prices WHERE store_id = 1;
```

---

## 7. 핵심 용어 정리

| 용어 | 설명 |
|------|------|
| **ORM** | SQL을 직접 쓰지 않고 Kotlin 코드로 DB를 다루는 도구 (여기선 Exposed) |
| **엔드포인트** | 앱이 호출하는 서버 URL (e.g. `GET /api/stores/1`) |
| **트랜잭션** | 여러 DB 작업을 하나로 묶어 전부 성공하거나 전부 취소되도록 보장 |
| **외래키 (FK)** | 다른 테이블의 행을 참조하는 컬럼 (e.g. `store_id`가 `stores.id`를 참조) |
| **DDL** | 테이블 생성/변경 같은 DB 구조 정의 명령어 |
| **직렬화** | Kotlin 객체 → JSON 문자열로 변환하는 것 |
| **역직렬화** | JSON 문자열 → Kotlin 객체로 변환하는 것 |
