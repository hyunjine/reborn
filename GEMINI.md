# 프로젝트 가이드라인
이 문서는 현재 프로젝트의 아키텍처, 코드 스타일, 라이브러리 활용 패턴 및 컨벤션을 설명합니다.

## 1. Architecture Overview (Clean Architecture)

프로젝트는 **Clean Architecture**와 **Feature-by-Package** 전략을 결합하여 계층을 분리하고 유지보수성을 높였습니다.

- **Data Layer**: API 통신(`entity`, `data_source`) 및 데이터 영속화 담당.
- **Domain Layer (Model)**: UI에서 사용되는 순수 데이터 모델(`model`).
- **UI Layer**: Jetpack Compose 기반의 화면(`reser_list`, `reser_detail`) 및 ViewModel.

### Modules
- `:app`: 메인 어플리케이션 모듈.
- `:lib:response`: API 응답 처리를 위한 공통 인터페이스(`Return<T>`).
- `:lib:retrofit-engine`: Retrofit 설정을 위한 인터셉터 및 어댑터.

---

## 2. UI Layer Conventions (Jetpack Compose)

### Screen & Navigation Pattern
가장 독특한 컨벤션으로, 모든 화면은 `NavKey`를 구현하는 `@Serializable` 클래스나 객체로 정의됩니다.

- **Screen Definition**: `operator fun invoke`를 사용하여 Composable 함수를 정의합니다.
  - 첫 번째 `invoke`: Koin ViewModel을 주입받고 이벤트를 처리하는 **Stateless/Stateful Wrapper**.
  - 두 번째 `invoke`: 실제 UI를 그리는 **Pure Composable**.

```kotlin
@Serializable
data class ReservationDetailScreen(
  val serverId: Long
): NavKey {
  sealed interface UiEvent {
    data class RejectReservationState(val isShow: Boolean): UiEvent
    data class AcceptReservationState(val isShow: Boolean): UiEvent
    data object RequestReservationDetail: UiEvent
    data class HandleRequest(val id: Long, val status: ReservationStatus): UiEvent
  }

  @Composable
  operator fun invoke(
    // Koin ViewModel 주입 (인자가 필요한 경우 parametersOf 사용)
    viewModel: ReservationDetailViewModel = koinViewModel { parametersOf(this) },
    onBack: () -> Unit = {}
  ) {
    val detail by viewModel.detail.collectAsStateWithLifecycle()
    val isShowRejectReservationPopup by viewModel.isShowRejectReservationPopup.collectAsStateWithLifecycle()
    val isShowAcceptReservationPopup by viewModel.isShowAcceptReservationPopup.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
      viewModel.successHandleRequest.collect { onBack() }
    }

    invoke(
      detail = detail,
      onBack = onBack,
      isShowRejectReservationPopup = isShowRejectReservationPopup,
      isShowAcceptReservationPopup = isShowAcceptReservationPopup,
      retryReservationDetail = { viewModel.event(UiEvent.RequestReservationDetail) },
      requestRejectReservationState = { viewModel.event(UiEvent.RejectReservationState(it)) },
      requestAcceptReservationState = { viewModel.event(UiEvent.AcceptReservationState(it)) },
      handleRequest = { id, status -> viewModel.event(UiEvent.HandleRequest(id, status)) },
    )
  }

  @Composable
  operator fun invoke(
    detail: ReservationDetailModel,
    isShowRejectReservationPopup: Boolean,
    isShowAcceptReservationPopup: Boolean,
    retryReservationDetail: () -> Unit = { },
    onBack: () -> Unit = { },
    requestRejectReservationState: (isShow: Boolean) -> Unit = { },
    requestAcceptReservationState: (isShow: Boolean) -> Unit = { },
    handleRequest: (id: Long, status: ReservationStatus) -> Unit = { _, _ -> },
  ) {
    // UI 구현...
  }
}
```

---

## 3. State Management (ViewModel & Flow)

- **Encapsulation**: `MutableStateFlow`나 `MutableSharedFlow`는 반드시 `private`으로 선언하며, 외부에 노출할 때는 `asStateFlow()` 또는 `asSharedFlow()`를 사용하여 ReadOnly Flow로 노출합니다.
- **Event-Driven**: `event(UiEvent)` 함수를 통해 모든 UI 액션을 처리하며, 비즈니스 로직은 `viewModelScope` 내에서 Flow Operator를 사용하여 선언적으로 구현합니다.

```kotlin
@KoinViewModel
class ReservationDetailViewModel(
  private val arg: ReservationDetailScreen, // Koin에서 인자로 주입됨
  private val repository: ReservationRepository
): ViewModel() {

  private val _uiEvent = MutableSharedFlow<ReservationDetailScreen.UiEvent>()

  /**
   * 예약 요청이 성공적으로 처리되면 발생되는 이벤트입니다.
   * 외부에는 SharedFlow(ReadOnly)로 노출합니다.
   */
  val successHandleRequest: SharedFlow<Unit> = _uiEvent
    .filterIsInstance<ReservationDetailScreen.UiEvent.HandleRequest>()
    .map { (id, status) ->
      repository.addHandledRequest(id = id, status = status)
    }.shareIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000))

  // ... 기타 상태(isShowingPopup 등)도 동일하게 private Mutable -> public Immutable 패턴 준수

  fun event(event: ReservationDetailScreen.UiEvent) {
    viewModelScope.launch {
      _uiEvent.emit(event)
    }
  }
}
```

---

## 5. Coding Style & Naming

- **Package Naming**: 기능 단위 패키징 시 `reser_list`, `reser_detail`과 같이 의미를 명확히 합니다.
- **File Naming**:
  - `*Screen.kt`: 화면 전체 구현체 (Stateful Wrapper + Stateless UI).
  - `*ViewModel.kt`: 화면 비즈니스 로직.
  - `*Model.kt`: UI 데이터 모델.
- **Immutability**: 리스트 전달 시 `kotlinx.collections.immutable.ImmutableList` 사용을 적극 권장합니다.

---

## 6. Testing

- **Screenshot Testing**: `Paparazzi` 라이브러리를 사용하여 UI 회귀 테스트를 수행합니다.
- **Koin Testing**: `KoinTest`를 활용하여 의존성 주입이 포함된 테스트를 진행합니다.

---

## 7. Recommended Tech Stack

- **Language**: Kotlin
- **DI**: Koin (with Annotations)
- **UI**: Jetpack Compose
- **Async**: Coroutines & Flow
- **Network**: Retrofit (or Ktor)
- **Serialization**: Kotlinx Serialization
- **Navigation**: Navigation 3 (Component based)

## 8. Composable
- 모든 Composable 함수에 대한 설명과 파라미터에 대한 설명을 kdoc으로 작성한다.
- 모든 Composable 함수에 대한 Preview 작성한다.

## 9. Commit
- 매 코드 변경 시 해당 변경 사항에 대한 commit 메세지를 작성한다.

## 10. Extra Convention
- Preview 어노테이션 사용 시 org.jetbrains.compose.ui.tooling.preview.Preview가 아닌 androidx.compose.ui.tooling.preview.Preview를 사용한다.
- Preview 어노테이션에는 반드시 `showBackground = true`를 설정한다. 예: `@Preview(showBackground = true)`

## 11. Figma
- 모든 vector 이미지들은 Android Vector Drawable XML로 변환하여 Drawable에 저장하여 사용한다.