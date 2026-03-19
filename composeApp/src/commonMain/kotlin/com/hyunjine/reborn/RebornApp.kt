package com.hyunjine.reborn

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.di.RebornAppKoin
import com.hyunjine.reborn.ui.login.LoginScreen
import com.hyunjine.reborn.ui.main.MainScreen
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

            // entryProvider: 각 NavKey에 대응하는 Composable 화면을 정의
            val entryProvider = entryProvider {
                screens(
                    onNavigate = { backStack.add(it) },
                    onBack = { backStack.removeLastOrNull() },
                )
            }

            // rememberDecoratedNavEntries: backStack + entryProvider + decorators를 결합하여
            // 상태 보존이 적용된 NavEntry 리스트를 생성
            val entries = rememberDecoratedNavEntries(
                backStack = backStack,
                entryDecorators = listOf(
                    // Composable 상태(rememberSaveable 등)를 화면별로 보존하는 데코레이터
                    rememberSaveableStateHolderNavEntryDecorator(),
                    // ViewModel을 화면별로 보존하는 데코레이터 (필요 시 활성화)
                    // rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider,
            )

            // NavDisplay: entries 기반으로 현재 화면을 렌더링
            NavDisplay(
                entries = entries,
                onBack = { backStack.removeLastOrNull() },
            )
        }
    }
}

/**
 * 슬라이드 전환 애니메이션 메타데이터
 * 화면 진입 시 Start 방향으로 슬라이드, 화면 복귀 시 End 방향으로 슬라이드
 */
private val slideTransitionMetadata =
    // 새 화면으로 전환할 때의 애니메이션
    NavDisplay.transitionSpec {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(300)
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(600)
        )
    } +
    // 뒤로가기로 이전 화면으로 돌아갈 때의 애니메이션
    NavDisplay.popTransitionSpec {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(300)
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(300)
        )
    } +
    // 제스처 기반 뒤로가기(predictive back) 시의 애니메이션
    NavDisplay.predictivePopTransitionSpec {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(300)
        ) togetherWith slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(300)
        )
    }

/**
 * 각 NavKey 타입에 대응하는 Composable 화면을 등록하는 확장 함수
 *
 * @param onNavigate 새로운 화면으로 이동할 때 호출되는 콜백
 * @param onBack 이전 화면으로 돌아갈 때 호출되는 콜백
 */
private fun EntryProviderScope<NavKey>.screens(
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit,
) {
    entry<LoginScreen> {
        LoginScreen()
    }
    // MainScreen은 시작 화면이므로 슬라이드 애니메이션 없이 표시
    entry<MainScreen> {
        MainScreen(
            onSearch = { TODO() },
            onNotification = { TODO() },
            onStoreDetail = { onNavigate(StoreDetailScreen(it)) },
            onSetting = { onNavigate(SettingScreen) },
            onRegisterStore = { TODO() },
        )
    }
    // 이하 화면들은 슬라이드 전환 애니메이션 적용
    entry<StoreDetailScreen>(metadata = slideTransitionMetadata) { screen ->
        screen.invoke(onBack = onBack)
    }
    entry<RegistStoreScreen>(metadata = slideTransitionMetadata) {
        RegistStoreScreen(onBack = onBack)
    }
    entry<SettingScreen>(metadata = slideTransitionMetadata) {
        SettingScreen(
            onBack = onBack,
            onNotificationSetting = { onNavigate(NotificationSettingScreen) }
        )
    }
    entry<NotificationSettingScreen>(metadata = slideTransitionMetadata) {
        NotificationSettingScreen(onBack = onBack)
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