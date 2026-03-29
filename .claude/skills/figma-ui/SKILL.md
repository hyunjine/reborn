---
name: figma-ui
description: Figma 디자인을 Compose UI로 단계별 구현하는 워크플로우
user_invocable: true
argument: Figma URL (필수). 여러 프레임을 구현할 경우 URL을 공백으로 구분하여 전달
---

# Figma → Compose UI 구현 워크플로우

Figma URL을 받아 디자인을 Compose UI로 단계별로 구현합니다.
각 단계가 완료될 때마다 사용자에게 결과를 보여주고 확인을 받은 후 다음 단계로 진행합니다.

## 입력 형식

- **단일 프레임**: `/figma-ui <URL>`
- **여러 프레임**: `/figma-ui <URL1> <URL2> <URL3>`
- URL에 여러 node-id가 포함된 경우도 지원

## Step 1: 디자인 분석

1. 각 Figma URL에서 fileKey와 nodeId를 파싱합니다. 여러 URL이 주어진 경우 각각 처리합니다.
2. 각 nodeId에 대해 `mcp__figma__get_design_context`를 호출하여 디자인 컨텍스트를 추출합니다. 여러 노드는 병렬로 추출합니다.
3. 추출된 정보를 사용자에게 정리하여 보여줍니다:
   - 색상 팔레트
   - 타이포그래피 (폰트, 크기, 두께)
   - 간격 및 레이아웃 구조
   - 아이콘/이미지 목록
4. **사용자 확인을 받은 후** 다음 단계로 진행합니다.

## Step 2: 아이콘 & 에셋 변환

1. 디자인에 포함된 벡터 아이콘을 Android Vector Drawable XML로 변환합니다.
2. 변환 시 반드시 **fill vs stroke 렌더링 차이**를 확인합니다.
3. 변환된 아이콘을 `composeApp/src/androidMain/res/drawable/`에 저장합니다.
4. 변환 결과를 사용자에게 보여주고, **품질이 불만족스러운 경우 사용자에게 먼저 확인**합니다.
5. **사용자 확인을 받은 후** 다음 단계로 진행합니다.

## Step 3: Compose UI 구현

다음 프로젝트 컨벤션을 반드시 준수합니다:

### 필수 컨벤션
- **Screen 패턴**: `NavKey`를 구현하는 `@Serializable` 클래스/객체로 정의
  - 첫 번째 `invoke`: Koin ViewModel 주입 + 이벤트 처리 (Stateful Wrapper)
  - 두 번째 `invoke`: 실제 UI (Pure Composable)
- **네비게이션**: 람다 콜백 사용 (예: `onBack: () -> Unit`). UiEvent sealed class로 네비게이션 처리 금지
- **클릭**: `Modifier.clickable` 대신 `Modifier.animClickable` 사용
- **버튼**: `Button` Composable 사용 금지. `Box` + `background` + `clip` + `animClickable` + `Text` 조합 사용
- **KDoc**: 모든 Composable 함수에 설명과 파라미터 KDoc 작성
- **Preview**: 모든 Composable에 Preview 작성. `@Preview(showBackground = true)` 필수. `androidx.compose.ui.tooling.preview.Preview` 사용
- **Immutability**: 리스트 전달 시 `ImmutableList` 사용
- **공통 컴포넌트**: 재사용 가능한 컴포넌트는 `common/component` 패키지에 생성

### 구현 순서
1. 필요한 Model 클래스 작성
2. Screen 클래스 작성 (두 개의 invoke)
3. ViewModel 작성 (private Mutable → public Immutable 패턴)
4. Preview 작성

## Step 4: 검증 & 커밋

1. `./gradlew compileKotlin`으로 빌드 검증합니다.
2. 빌드 실패 시 자동으로 수정합니다.
3. 변경된 파일에 대해 커밋합니다.