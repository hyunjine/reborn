package com.hyunjine.reborn.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.hyunjine.reborn.common.component.NavigationBar
import com.hyunjine.reborn.common.component.NavigationItem
import com.hyunjine.reborn.ui.home.HomeScreen
import com.hyunjine.reborn.ui.my.MyScreen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Serializable
object MainScreen: NavKey {
    val NavigationItemSaver = Saver<NavKey, String>(
        save = { Json.encodeToString(it) }, // JSON 문자열로 변환
        restore = { Json.decodeFromString(it) } // 다시 객체로 복구
    )

    @Composable
    operator fun invoke(
        onSearch: () -> Unit = {},
        onNotification: () -> Unit = {},
        onStoreDetail: (Long) -> Unit = {},
        onSetting: () -> Unit = {},
        onRegisterStore: () -> Unit = {},
    ) {
        val screenSaver = Saver<NavigationItem, String>(
            save = { it.label },
            restore = { label ->
                when (label) {
                    HomeScreen.label -> HomeScreen
                    MyScreen.label -> MyScreen
                    else -> throw IllegalArgumentException("Unknown label: $label")
                }
            }
        )
        var screen: NavigationItem by rememberSaveable(stateSaver = screenSaver) { mutableStateOf(HomeScreen) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier.weight(1F),
                contentAlignment = Alignment.TopCenter
            ) {
                when (screen) {
                    is HomeScreen -> {
                        HomeScreen(
                            onItemClick = onStoreDetail,
                        )
                    }
                    is MyScreen -> {
                        MyScreen(
                            onRegisterStore = onRegisterStore,
                            onStoreDetail = { TODO() },
                            onSetting = onSetting,
                            onNotice = { TODO() },
                            onTerms = { TODO() },
                            onCustomerService = { TODO() }
                        )
                    }
                }
            }
            NavigationBar(
                items = listOf(
                    HomeScreen,
                    MyScreen
                ).toImmutableList(),
                selectedItem = screen,
                onItemSelected = { screen = it },
            )
        }

    }
}