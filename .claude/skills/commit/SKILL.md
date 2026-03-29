---
name: commit
description: 변경사항을 검증하고 커밋하는 워크플로우
user_invocable: true
argument: 커밋 메시지 (선택). 생략 시 변경 내용을 분석하여 자동 생성
---

# 커밋 워크플로우

## Step 1: 변경사항 확인

1. `git status`로 변경된 파일 목록을 확인합니다.
2. `git diff`로 변경 내용을 확인합니다.
3. 변경사항이 없으면 커밋하지 않고 사용자에게 알립니다.

## Step 2: 누락 검증

1. 변경된 파일과 관련된 **모든 파일**이 함께 수정되었는지 확인합니다:
   - Model 변경 → ViewModel, Screen, Preview, 호출부
   - Screen 변경 → Preview
   - 공통 컴포넌트 변경 → 사용하는 모든 화면
2. 누락된 파일이 있으면 grep으로 사용처를 검색하여 함께 수정합니다.

## Step 3: 빌드 검증

1. `./gradlew compileKotlin`으로 빌드합니다.
2. 빌드 실패 시 오류를 수정하고 다시 빌드합니다.
3. 빌드 성공 후 다음 단계로 진행합니다.

## Step 4: 커밋

1. 변경된 **모든 파일**을 staging합니다. 하나도 빠뜨리지 않습니다.
2. 커밋 메시지 규칙:
   - **prefix 필수**: `feat:`, `fix:`, `refactor:`, `chore:`, `docs:`, `style:`, `test:`
   - **한글**로 작성합니다.
   - 변경의 "무엇"보다 "왜"에 초점을 맞춥니다.
   - 1~2문장으로 간결하게 작성합니다.
3. 예시:
   - `feat: 알림 리스트 화면 구현`
   - `fix: MapContent shadow가 clip에 잘리는 문제 수정`
   - `refactor: ReservationModel status 필드를 enum으로 변경`

## 주의사항

- 코드 변경 후 사용자가 요청할 때까지 기다리지 않고 **즉시 커밋**합니다.
- .env, credentials 등 민감한 파일은 커밋하지 않습니다.
- 커밋 메시지 끝에 Co-Authored-By를 포함합니다.
