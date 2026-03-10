package com.hyunjine.reborn

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
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
import com.hyunjine.reborn.di.RebornAppKoin
import com.hyunjine.reborn.ui.home.HomeScreen
import com.hyunjine.reborn.ui.my.MyScreen
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen
import com.hyunjine.reborn.ui.store_detail.StoreDetailScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.plugin.module.dsl.koinConfiguration


@Composable
@Preview
fun RebornApp() {
    KoinApplication(configuration = koinConfiguration<RebornAppKoin>()) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val backStack = rememberNavBackStack(configuration = navConfig, HomeScreen)
                NavDisplay(
                    backStack = backStack,
                    transitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(300)
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(600)
                        )
                    },
                    popTransitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        )
                    },
                    predictivePopTransitionSpec = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        ) togetherWith slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(300)
                        )
                    },
                    entryProvider = entryProvider {
                        entry<HomeScreen> {
                            HomeScreen(
                                onCenterClick = { id -> backStack.add(StoreDetailScreen(id)) },
                                onNavClick = { route ->
                                    when (route) {
                                        "my" -> backStack.add(MyScreen)
                                    }
                                }
                            )
                        }
                        entry<StoreDetailScreen> { screen ->
                            screen.invoke(onBack = { backStack.removeLastOrNull() })
                        }
                        entry<RegistStoreScreen> {
                            RegistStoreScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        entry<MyScreen> {
                            MyScreen(
                                onRegisterStore = { backStack.add(RegistStoreScreen) },
                                onNavClick = { route ->
                                    when (route) {
                                        "home" -> backStack.removeLastOrNull()
                                    }
                                }
                            )
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
            subclass(MyScreen::class, MyScreen.serializer())
        }
    }
}