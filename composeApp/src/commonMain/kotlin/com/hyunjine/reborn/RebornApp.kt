package com.hyunjine.reborn

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.di.RebornAppKoin
import com.hyunjine.reborn.ui.home.HomeScreen
import com.hyunjine.reborn.ui.login.LoginScreen
import com.hyunjine.reborn.ui.main.MainScreen
import com.hyunjine.reborn.ui.my.MyScreen
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen
import com.hyunjine.reborn.ui.setting.SettingScreen
import com.hyunjine.reborn.ui.setting.noti.NotificationSettingScreen
import com.hyunjine.reborn.ui.store_detail.StoreDetailScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
@Preview
fun RebornApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }
    KoinApplication(configuration = koinConfiguration<RebornAppKoin>()) {
        RebornTheme {
            val backStack = rememberNavBackStack(configuration = navConfig, MainScreen)
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
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    // Add the default decorators for managing scenes and saving state
                    rememberSaveableStateHolderNavEntryDecorator(),
                    // Then add the view model store decorator
                    // rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<LoginScreen> {
                        LoginScreen()
                    }
                    entry<MainScreen> {
                        MainScreen(
                            onSearch = { TODO() },
                            onNotification = { TODO() },
                            onStoreDetail = { backStack.add(StoreDetailScreen(it)) },
                            onSetting = { backStack.add(SettingScreen) },
                            onRegisterStore = { TODO() },
                        )
                    }
                    entry<StoreDetailScreen> { screen ->
                        screen.invoke(onBack = { backStack.removeLastOrNull() })
                    }
                    entry<RegistStoreScreen> {
                        RegistStoreScreen(onBack = { backStack.removeLastOrNull() })
                    }
                    entry<SettingScreen> {
                        SettingScreen(
                            onBack = { backStack.removeLastOrNull() },
                            onNotificationSetting = { backStack.add(SettingScreen) }
                        )
                    }
                    entry<NotificationSettingScreen> {
                        NotificationSettingScreen(
                            onBack = { backStack.removeLastOrNull() }
                        )
                    }
                }
            )
        }
    }
}

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(LoginScreen::class, LoginScreen.serializer())
            subclass(MainScreen::class, MainScreen.serializer())
            subclass(StoreDetailScreen::class, StoreDetailScreen.serializer())
            subclass(RegistStoreScreen::class, RegistStoreScreen.serializer())
            subclass(SettingScreen::class, SettingScreen.serializer())
            subclass(NotificationSettingScreen::class, NotificationSettingScreen.serializer())
        }
    }
}