package com.hyunjine.reborn.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hyunjine.reborn.data.Location
import com.hyunjine.reborn.common.component.NavigationItem
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.common.theme.color
import com.hyunjine.reborn.common.theme.typography
import com.hyunjine.reborn.data.store.model.Distance
import com.hyunjine.reborn.data.store.model.MatterModel
import com.hyunjine.reborn.data.store.model.StoreModel
import com.hyunjine.reborn.util.readable
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.icon_24_home
import reborn.composeapp.generated.resources.icon_24_location
import reborn.composeapp.generated.resources.icon_24_location_header
import reborn.composeapp.generated.resources.icon_24_market_price
import reborn.composeapp.generated.resources.icon_24_notification
import reborn.composeapp.generated.resources.icon_24_profile
import reborn.composeapp.generated.resources.icon_24_search

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
        state: StoreState,
        modifier: Modifier = Modifier.fillMaxSize(),
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Column(modifier = modifier) {
            HomeTopBar(
                location = location.toString(),
                onSearchClick = { onEvent(UiEvent.SearchClicked) },
                onNotificationClick = { onEvent(UiEvent.NotificationClicked) }
            )
            when (state) {
                is StoreState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = color.green500
                        )
                    }
                }
                is StoreState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = state.stores,
                            key = { it.id }
                        ) { store ->
                            GarbageCenterItem(
                                store = store,
                                onClick = { onEvent(UiEvent.StoreClicked(store.id)) }
                            )
                        }
                    }
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
 * 홈 화면의 상단 바입니다.
 * @param location 현재 표시될 위치 텍스트입니다.
 * @param onSearchClick 검색 아이콘 클릭 시 호출되는 콜백입니다.
 * @param onNotificationClick 알림 아이콘 클릭 시 호출되는 콜백입니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    location: String,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1F)
                .clickable { /* TODO: Change location */ }
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_24_location_header),
                contentDescription = null,
                tint = color.gray900,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = location,
                style = typography.titleSemibold16,
                color = color.gray900,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Row {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(Res.drawable.icon_24_search),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            }
            BadgedBox(
                badge = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color.green500, CircleShape)
                    )
                }
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_24_notification),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * 홈 화면의 필터 칩 목록입니다.
 * @param filters 표시할 필터 이름 목록입니다.
 * @param selectedFilter 현재 선택된 필터입니다.
 * @param onFilterSelected 필터 선택 시 호출되는 콜백입니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChips(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        items(filters) { filter ->
            val isSelected = filter == selectedFilter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter, style = typography.bodyMedium14) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.green500,
                    selectedLabelColor = Color.White,
                    containerColor = color.gray100,
                    labelColor = color.gray800
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

/**
 * 개별 고물상 정보를 표시하는 아이템입니다.
 * @param store 고물상 데이터 모델입니다.
 * @param onClick 아이템 클릭 시 호출되는 콜백입니다.
 */
@Composable
fun GarbageCenterItem(
    store: StoreModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = store.imageUrl,
            contentDescription = "${store.name} 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(color.gray100)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = store.name,
                style = typography.headingSemibold18,
                color = color.gray900
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_24_location),
                    contentDescription = null,
                    tint = color.gray600,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = store.distance.toString(),
                    style = typography.bodyRegular14,
                    color = color.gray600
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            store.prices.forEach { price ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = price.name,
                        style = typography.bodyRegular14,
                        color = color.gray900
                    )
                    Text(
                        text = price.price.readable(),
                        style = typography.bodySemibold14,
                        color = color.gray900
                    )
                }
            }
        }
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
            state = StoreState.Loaded(
                stores = List(10) {
                    StoreModel(
                        id = it.toLong(),
                        name = "서울고물상",
                        imageUrl = "",
                        distance = Distance.meters(20),
                        prices = persistentListOf(
                            MatterModel("고철", 59040),
                            MatterModel("고철", 540),
                        )
                    )
                }.toImmutableList()
            )
        )
    }
}
