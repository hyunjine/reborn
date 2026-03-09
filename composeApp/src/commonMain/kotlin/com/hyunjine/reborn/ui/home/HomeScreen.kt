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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reborn.composeapp.generated.resources.Res
import reborn.composeapp.generated.resources.ic_location
import reborn.composeapp.generated.resources.ic_location_header
import reborn.composeapp.generated.resources.ic_nav_home
import reborn.composeapp.generated.resources.ic_nav_my
import reborn.composeapp.generated.resources.ic_nav_price
import reborn.composeapp.generated.resources.ic_notification
import reborn.composeapp.generated.resources.ic_search

/**
 * Re-born 앱의 홈 화면입니다.
 * 고물상 목록과 필터 칩을 표시합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Serializable
object HomeScreen : NavKey {

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
        data class CenterClicked(val id: Long) : UiEvent
        
        /**
         * 검색 아이콘이 클릭되었을 때 발생하는 이벤트입니다.
         */
        data object SearchClicked : UiEvent
        
        /**
         * 알림 아이콘이 클릭되었을 때 발생하는 이벤트입니다.
         */
        data object NotificationClicked : UiEvent

        /**
         * 하단 네비게이션 아이템이 클릭되었을 때 발생하는 이벤트입니다.
         * @param route 이동할 경로(route)입니다.
         */
        data class NavClicked(val route: String) : UiEvent
    }

    /**
     * 홈 화면의 Stateful Wrapper입니다.
     * @param viewModel Koin을 통해 주입되는 ViewModel입니다.
     * @param onCenterClick 고물상 클릭 시 호출되는 콜백입니다.
     * @param onNavClick 네비게이션 아이템 클릭 시 호출되는 콜백입니다.
     */
    @Composable
    operator fun invoke(
        viewModel: HomeViewModel = koinViewModel(),
        onCenterClick: (Long) -> Unit = {},
        onNavClick: (String) -> Unit = {}
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        invoke(
            uiState = uiState,
            onEvent = { event ->
                when (event) {
                    is UiEvent.CenterClicked -> onCenterClick(event.id)
                    is UiEvent.NavClicked -> onNavClick(event.route)
                    else -> viewModel.event(event)
                }
            }
        )
    }

    /**
     * 홈 화면의 Stateless UI 구현체입니다.
     * @param uiState 현재 화면의 UI 상태입니다.
     * @param onEvent UI 이벤트 처리를 위한 콜백입니다.
     */
    @Composable
    operator fun invoke(
        uiState: HomeModel,
        onEvent: (UiEvent) -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                HomeTopBar(
                    location = uiState.location,
                    onSearchClick = { onEvent(UiEvent.SearchClicked) },
                    onNotificationClick = { onEvent(UiEvent.NotificationClicked) }
                )
            },
            bottomBar = {
                HomeBottomNavigation(
                    selectedRoute = "home",
                    onNavClick = { onEvent(UiEvent.NavClicked(it)) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                FilterChips(
                    filters = uiState.filters,
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = { onEvent(UiEvent.FilterSelected(it)) }
                )
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.centers) { center ->
                        GarbageCenterItem(
                            center = center,
                            onClick = { onEvent(UiEvent.CenterClicked(center.id)) }
                        )
                    }
                }
            }
        }
    }
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
            icon = { Icon(painterResource(Res.drawable.ic_nav_home), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("홈") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF009966),
                selectedTextColor = Color(0xFF009966),
                unselectedIconColor = Color(0xFF6A7282),
                unselectedTextColor = Color(0xFF6A7282),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == "price",
            onClick = { onNavClick("price") },
            icon = { Icon(painterResource(Res.drawable.ic_nav_price), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("시세") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF009966),
                selectedTextColor = Color(0xFF009966),
                unselectedIconColor = Color(0xFF6A7282),
                unselectedTextColor = Color(0xFF6A7282),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedRoute == "my",
            onClick = { onNavClick("my") },
            icon = { Icon(painterResource(Res.drawable.ic_nav_my), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("내정보") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF009966),
                selectedTextColor = Color(0xFF009966),
                unselectedIconColor = Color(0xFF6A7282),
                unselectedTextColor = Color(0xFF6A7282),
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
            modifier = Modifier.clickable { /* TODO: Change location */ }
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_location_header),
                contentDescription = null,
                tint = Color(0xFF101828),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = location,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF101828)
            )
        }
        
        Row {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(painterResource(Res.drawable.ic_notification), contentDescription = "Notifications", modifier = Modifier.size(24.dp))
                }
                // 알림 배지
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF22C55E), CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-10).dp, y = 8.dp)
                )
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
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF22C55E),
                    selectedLabelColor = Color.White,
                    containerColor = Color(0xFFF3F4F6),
                    labelColor = Color(0xFF364153)
                ),
                border = null,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

/**
 * 개별 고물상 정보를 표시하는 아이템입니다.
 * @param center 고물상 데이터 모델입니다.
 * @param onClick 아이템 클릭 시 호출되는 콜백입니다.
 */
@Composable
fun GarbageCenterItem(
    center: GarbageCenterModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 이미지 플레이스홀더
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF3F4F6))
        ) {
            // 실제 이미지 로딩 로직이 여기에 들어갑니다.
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = center.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF101828)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_location),
                    contentDescription = null,
                    tint = Color(0xFF6A7282),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = center.distance,
                    fontSize = 14.sp,
                    color = Color(0xFF6A7282)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            center.prices.forEach { price ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = price.name,
                        fontSize = 14.sp,
                        color = Color(0xFF4A5565)
                    )
                    Text(
                        text = price.price,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF22C55E)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeModel(
            location = "개포동",
            filters = persistentListOf(),
            selectedFilter = "전체",
            centers = persistentListOf(
                GarbageCenterModel(
                    id = 1,
                    name = "서울고물상",
                    imageUrl = "",
                    distance = "0.2km",
                    prices = persistentListOf(
                        PriceModel("고철", "450원/kg"),
                        PriceModel("알루미늄", "1,800원/kg")
                    )
                ),
                GarbageCenterModel(
                    id = 2,
                    name = "금성스크랩",
                    imageUrl = "",
                    distance = "0.8km",
                    prices = persistentListOf(
                        PriceModel("고철", "480원/kg"),
                        PriceModel("스텐", "1,200원/kg")
                    )
                ),
                GarbageCenterModel(
                    id = 3,
                    name = "대한재활용센터",
                    imageUrl = "",
                    distance = "1.2km",
                    prices = persistentListOf(
                        PriceModel("구리", "8,800원/kg"),
                        PriceModel("황동", "5,200원/kg")
                    )
                ),
                GarbageCenterModel(
                    id = 4,
                    name = "진성고물상",
                    imageUrl = "",
                    distance = "1.5km",
                    prices = persistentListOf(
                        PriceModel("고철", "470원/kg"),
                        PriceModel("구리", "8,600원/kg")
                    )
                ),
                GarbageCenterModel(
                    id = 5,
                    name = "한강재활용",
                    imageUrl = "",
                    distance = "2.3km",
                    prices = persistentListOf(
                        PriceModel("기판", "12,000원/kg"),
                        PriceModel("알루미늄", "1,750원/kg")
                    )
                ),
                GarbageCenterModel(
                    id = 6,
                    name = "강남스크랩",
                    imageUrl = "",
                    distance = "3.1km",
                    prices = persistentListOf(
                        PriceModel("구리", "8,700원/kg"),
                        PriceModel("스텐", "1,300원/kg")
                    )
                )
            )
        )
    )
}
