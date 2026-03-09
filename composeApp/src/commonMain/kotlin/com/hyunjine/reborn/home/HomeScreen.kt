package com.hyunjine.reborn.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyunjine.reborn.core.navigation.NavKey
import kotlinx.serialization.Serializable
import androidx.compose.ui.tooling.preview.Preview
import reborn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

/**
 * HomeScreen for the Re-born app.
 * Displays a list of garbage centers and filter chips.
 */
@Serializable
object HomeScreen : NavKey {

    /**
     * UiEvents for the HomeScreen.
     */
    sealed interface UiEvent {
        /**
         * Event when a filter is selected.
         * @param filter The name of the filter.
         */
        data class FilterSelected(val filter: String) : UiEvent
        
        /**
         * Event when a center is clicked.
         * @param id The ID of the center.
         */
        data class CenterClicked(val id: Long) : UiEvent
        
        /**
         * Event for search click.
         */
        data object SearchClicked : UiEvent
        
        /**
         * Event for notification click.
         */
        data object NotificationClicked : UiEvent

        /**
         * Event for bottom navigation click.
         * @param route The route to navigate to.
         */
        data class NavClicked(val route: String) : UiEvent
    }

    /**
     * Stateful Wrapper for the HomeScreen.
     * @param viewModel The Koin ViewModel.
     * @param onCenterClick Callback when a center is clicked.
     */
    @Composable
    operator fun invoke(
        viewModel: HomeViewModel = koinViewModel(),
        onCenterClick: (Long) -> Unit = {}
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        invoke(
            uiState = uiState,
            onEvent = { event ->
                when (event) {
                    is UiEvent.CenterClicked -> onCenterClick(event.id)
                    else -> viewModel.event(event)
                }
            }
        )
    }

    /**
     * Stateless UI for the HomeScreen.
     * @param uiState The current UI state.
     * @param onEvent Callback for UI events.
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
 * Bottom Navigation for the HomeScreen.
 * @param selectedRoute The currently selected route.
 * @param onNavClick Callback for navigation item clicks.
 */
@Composable
fun HomeBottomNavigation(
    selectedRoute: String,
    onNavClick: (String) -> Unit
) {
    NavigationBar(
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
 * TopBar for the HomeScreen.
 * @param location The current location text.
 * @param onSearchClick Callback for search icon click.
 * @param onNotificationClick Callback for notification icon click.
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
                Box(modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_search_vector1),
                        contentDescription = "Search",
                        modifier = Modifier.padding(3.dp).fillMaxSize()
                    )
                    Icon(
                        painter = painterResource(Res.drawable.ic_search_vector2),
                        contentDescription = null,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp).size(6.dp)
                    )
                }
            }
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(painterResource(Res.drawable.ic_notification), contentDescription = "Notifications", modifier = Modifier.size(24.dp))
                }
                // Notification Badge
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
 * Filter chips for the HomeScreen.
 * @param filters List of filter names.
 * @param selectedFilter The currently selected filter.
 * @param onFilterSelected Callback when a filter is selected.
 */
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
 * Item for a garbage center.
 * @param center The garbage center data model.
 * @param onClick Callback when the item is clicked.
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
        // Image Placeholder
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF3F4F6))
        ) {
            // Actual image loading would go here
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
                )
            )
        )
    )
}
