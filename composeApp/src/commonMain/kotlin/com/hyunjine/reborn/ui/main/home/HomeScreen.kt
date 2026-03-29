package com.hyunjine.reborn.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyunjine.reborn.common.component.FloatButton
import com.hyunjine.reborn.common.component.HomeAppBar
import com.hyunjine.reborn.common.component.RequestLocationPermission
import com.hyunjine.reborn.common.component.HomeAppBarStyle
import com.hyunjine.reborn.common.component.KakaoMapView
import com.hyunjine.reborn.common.component.NavigationItem
import com.hyunjine.reborn.common.component.StoreCard
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.util.animClickable
import com.hyunjine.reborn.common.util.shadowWeak
import com.hyunjine.reborn.data.ApiResponse
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.data.store.model.Distance
import com.hyunjine.reborn.data.store.model.MatterModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.util.ImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_home
import reborn.composeapp.generated.resources.icon_24_list
import reborn.composeapp.generated.resources.icon_24_map
import reborn.composeapp.generated.resources.icon_24_target

/**
 * Re-born 앱의 홈 화면입니다.
 * 리스트 모드와 지도 모드를 전환하여 고물상 목록을 표시합니다.
 */
@Serializable
object HomeScreen : NavigationItem {

    /**
     * 홈 화면에서 발생하는 UI 이벤트들입니다.
     */
    sealed interface UiEvent {
        /**
         * 고물상이 클릭되었을 때 발생하는 이벤트입니다.
         * @param id 클릭된 고물상의 ID입니다.
         */
        data class StoreClicked(val id: Long) : UiEvent

        /**
         * 검색 아이콘이 클릭되었을 때 발생하는 이벤트입니다.
         */
        data object SearchClicked : UiEvent

        /**
         * 알림 아이콘이 클릭되었을 때 발생하는 이벤트입니다.
         */
        data object NotificationClicked : UiEvent
    }

    /**
     * 홈 화면의 Stateful Wrapper입니다.
     * @param viewModel Koin을 통해 주입되는 ViewModel입니다.
     * @param onItemClick 고물상 클릭 시 호출되는 콜백입니다.
     * @param modifier Modifier입니다.
     */
    /** 서울 시청 좌표 (권한 거부 시 기본값) */
    private val DEFAULT_LOCATION = Location(37.5666, 126.9784)

    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        viewModel: HomeViewModel = koinViewModel(),
        onItemClick: (Long) -> Unit = {},
    ) {
        RequestLocationPermission(onResult = {})

        val state by viewModel.state.collectAsStateWithLifecycle()
        val location by viewModel.location.collectAsStateWithLifecycle()
        invoke(
            modifier = modifier,
            location = location ?: DEFAULT_LOCATION,
            state = state,
            onEvent = { event ->
                when (event) {
                    is UiEvent.StoreClicked -> onItemClick(event.id)
                    else -> viewModel.event(event)
                }
            }
        )
    }

    /**
     * 홈 화면의 Stateless UI 구현체입니다.
     * @param location 현재 위치 정보입니다.
     * @param state 현재 화면의 UI 상태입니다.
     * @param modifier Modifier입니다.
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     */
    @Composable
    operator fun invoke(
        location: Location,
        state: ApiResponse<ImmutableList<StoreModel>>,
        modifier: Modifier = Modifier.fillMaxSize(),
        onEvent: (UiEvent) -> Unit = {}
    ) {
        var isMapMode by rememberSaveable { mutableStateOf(false) }

        Box(modifier = modifier) {
            MapContent(
                location = location,
                onSearchClick = { onEvent(UiEvent.SearchClicked) },
                onNotificationClick = { onEvent(UiEvent.NotificationClicked) },
                onToggleMode = { isMapMode = false },
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (isMapMode) 1f else 0f)
                    .alpha(if (isMapMode) 1f else 0f)
            )
            ListContent(
                state = state,
                onSearchClick = { onEvent(UiEvent.SearchClicked) },
                onNotificationClick = { onEvent(UiEvent.NotificationClicked) },
                onStoreClick = { onEvent(UiEvent.StoreClicked(it)) },
                onToggleMode = { isMapMode = true },
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (!isMapMode) 1f else 0f)
                    .alpha(if (!isMapMode) 1f else 0f)
            )
        }
    }

    override val icon: DrawableResource = Res.drawable.icon_24_home
    override val label: String = "홈"
}

/**
 * 홈 화면의 리스트 모드 콘텐츠입니다.
 * @param state 현재 화면의 UI 상태입니다.
 * @param onSearchClick 검색 클릭 시 호출되는 콜백입니다.
 * @param onNotificationClick 알림 클릭 시 호출되는 콜백입니다.
 * @param onStoreClick 고물상 클릭 시 호출되는 콜백입니다.
 * @param onToggleMode 지도 모드 전환 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
private fun ListContent(
    state: ApiResponse<ImmutableList<StoreModel>>,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onStoreClick: (Long) -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeAppBar(
                onSearchClick = onSearchClick,
                onNotificationClick = onNotificationClick,
                style = HomeAppBarStyle.Flat,
                showDivider = isScrolled
            )
            when (state) {
                is ApiResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = color.green500
                        )
                    }
                }
                is ApiResponse.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 28.dp),
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                    ) {
                        items(
                            items = state.data,
                            key = { it.id }
                        ) { store ->
                            StoreCard(
                                store = store,
                                onClick = { onStoreClick(store.id) }
                            )
                        }
                    }
                }
                is ApiResponse.Error -> { }
            }
        }

        FloatButton(
            icon = Res.drawable.icon_24_map,
            contentDescription = "지도 보기",
            onClick = onToggleMode,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}

/**
 * 홈 화면의 지도 모드 콘텐츠입니다.
 * @param location 현재 위치 정보입니다.
 * @param onSearchClick 검색 클릭 시 호출되는 콜백입니다.
 * @param onNotificationClick 알림 클릭 시 호출되는 콜백입니다.
 * @param onToggleMode 리스트 모드 전환 클릭 시 호출되는 콜백입니다.
 * @param modifier Modifier입니다.
 */
@Composable
private fun MapContent(
    location: Location,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var moveToMyLocation by remember { mutableIntStateOf(0) }

    Box(modifier = modifier) {
        KakaoMapView(
            location = location,
            moveToMyLocation = moveToMyLocation,
            modifier = Modifier.fillMaxSize()
        )

        HomeAppBar(
            onSearchClick = onSearchClick,
            onNotificationClick = onNotificationClick,
            style = HomeAppBarStyle.Float,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
                .size(36.dp)
                .shadowWeak(shape = CircleShape)
                .clip(CircleShape)
                .background(color.white)
                .animClickable(shape = CircleShape, onClick = { moveToMyLocation++ }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_target),
                contentDescription = "내 위치",
                modifier = Modifier.size(24.dp),
                tint = color.gray900
            )
        }

        FloatButton(
            icon = Res.drawable.icon_24_list,
            contentDescription = "리스트 보기",
            onClick = onToggleMode,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}

/**
 * 홈 화면 리스트 모드 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
private fun HomeScreenListPreview() {
    RebornTheme {
        HomeScreen(
            location = Location(37.5666, 126.9784),
            state = ApiResponse.Success(
                data = ImmutableList(6) {
                    StoreModel(
                        id = it.toLong(),
                        name = "서울철강",
                        imageUrl = "",
                        distance = Distance.meters(3100),
                        prices = persistentListOf(
                            MatterModel("철근", price = 7200, unit = "kg"),
                            MatterModel("동", price = 7200, unit = "kg"),
                        )
                    )
                }.toImmutableList()
            )
        )
    }
}
