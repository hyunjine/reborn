package com.hyunjine.reborn.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyunjine.reborn.common.component.HomeTopBar
import com.hyunjine.reborn.common.component.NavigationItem
import com.hyunjine.reborn.common.component.StoreListItem
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
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
import reborn.composeapp.generated.resources.icon_24_market_price
import reborn.composeapp.generated.resources.icon_24_profile

/**
 * Re-born 앱의 홈 화면입니다.
 * 고물상 목록과 필터 칩을 표시합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Serializable
object HomeScreen : NavigationItem {

    /**
     * 홈 화면에서 발생하는 UI 이벤트들입니다.
     */
    sealed interface UiEvent {
        /**
         * 필터가 선택되었을 때 발생하는 이벤트입니다.
         * @param filter 선택된 필터의 이름입니다.
         */
        data class FilterSelected(val filter: String) : UiEvent

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
     */
    @Composable
    operator fun invoke(
        modifier: Modifier = Modifier,
        viewModel: HomeViewModel = koinViewModel(),
        onItemClick: (Long) -> Unit = {},
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val location by viewModel.location.collectAsStateWithLifecycle()
        invoke(
            modifier = modifier,
            location = location,
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
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     */
    @Composable
    operator fun invoke(
        location: Location?,
        state: ApiResponse<ImmutableList<StoreModel>>,
        modifier: Modifier = Modifier.fillMaxSize(),
        onEvent: (UiEvent) -> Unit = {}
    ) {
        val listState = rememberLazyListState()
        val isScrolled by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
        }

        Column(modifier = modifier) {
            HomeTopBar(
                isScrolled = isScrolled,
                onLocationClick = { /* TODO: Change location */ },
                onSearchClick = { onEvent(UiEvent.SearchClicked) },
                onNotificationClick = { onEvent(UiEvent.NotificationClicked) }
            )
            when (state) {
                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = color.green500
                        )
                    }
                }
                is ApiResponse.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                    ) {
                        items(
                            items = state.data,
                            key = { it.id }
                        ) { store ->
                            StoreListItem(
                                store = store,
                                onClick = { onEvent(UiEvent.StoreClicked(store.id)) }
                            )
                        }
                    }
                }
                is ApiResponse.Error -> {

                }
            }
        }
    }

    override val icon: DrawableResource = Res.drawable.icon_24_home
    override val label: String = "홈"
}

/**
 * 홈 화면의 하단 네비게이션 바입니다.
 * @param selectedRoute 현재 선택된 경로입니다.
 * @param onNavClick 네비게이션 아이템 클릭 시 호출되는 콜백입니다.
 */
@Composable
fun HomeBottomNavigation(
    selectedRoute: String,
    onNavClick: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedRoute == "home",
            onClick = { onNavClick("home") },
            icon = { Icon(painterResource(Res.drawable.icon_24_home), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("홈", style = typography.captionMedium12) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = color.green700,
                selectedTextColor = color.green700,
                unselectedIconColor = color.gray600,
                unselectedTextColor = color.gray600,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == "price",
            onClick = { onNavClick("price") },
            icon = { Icon(painterResource(Res.drawable.icon_24_market_price), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("시세", style = typography.captionMedium12) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = color.green700,
                selectedTextColor = color.green700,
                unselectedIconColor = color.gray600,
                unselectedTextColor = color.gray600,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == "my",
            onClick = { onNavClick("my") },
            icon = { Icon(painterResource(Res.drawable.icon_24_profile), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("내정보", style = typography.captionMedium12) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = color.green700,
                selectedTextColor = color.green700,
                unselectedIconColor = color.gray600,
                unselectedTextColor = color.gray600,
                indicatorColor = Color.Transparent
            )
        )
    }
}

/**
 * 홈 화면 프리뷰입니다.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RebornTheme {
        HomeScreen(
            location = null,
            state = ApiResponse.Success(
                data = ImmutableList(10) {
                    StoreModel(
                        id = it.toLong(),
                        name = "서울고물상",
                        imageUrl = "",
                        distance = Distance.meters(20),
                        prices = persistentListOf(
                            MatterModel("고철", 59040),
                            MatterModel("고철", 540),
                            MatterModel("고철", 540),
                            MatterModel("고철", 540),
                        )
                    )
                }.toImmutableList()
            )
        )
    }
}
