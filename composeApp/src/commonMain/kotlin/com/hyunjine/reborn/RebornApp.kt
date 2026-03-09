package com.hyunjine.reborn

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.hyunjine.reborn.core.di.appModule
import com.hyunjine.reborn.home.HomeScreen
import com.hyunjine.reborn.regist_store.RegistStoreScreen
import com.hyunjine.reborn.store_detail.StoreDetailScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinApplication

@Composable
@Preview
fun RebornApp() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val backStack = rememberNavBackStack(configuration = navConfig, HomeScreen)
                NavDisplay(
                    backStack = backStack,
                    entryProvider = entryProvider {
                        entry<HomeScreen> {
                            HomeScreen(
                                onCenterClick = { id -> backStack.add(StoreDetailScreen(id)) }
                            )
                        }
                        entry<StoreDetailScreen> { screen ->
                            screen.invoke(onBack = { backStack.removeLastOrNull() })
                        }
                        entry<RegistStoreScreen> {
                            RegistStoreScreen(onBack = { backStack.removeLastOrNull() })
                        }
                    }
                )
            }
        }
    }
}

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(HomeScreen::class, HomeScreen.serializer())
            subclass(StoreDetailScreen::class, StoreDetailScreen.serializer())
            subclass(RegistStoreScreen::class, RegistStoreScreen.serializer())
        }
    }
}