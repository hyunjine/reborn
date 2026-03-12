package com.hyunjine.reborn

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.hyunjine.reborn.common.theme.RebornTheme
import com.hyunjine.reborn.di.RebornAppKoin
import com.hyunjine.reborn.ui.regist_store.RegistStoreScreen
import com.hyunjine.reborn.ui.store_detail.StoreDetailScreen
import org.koin.compose.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)

        setContent {
            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context)
                    .components {
                        add(KtorNetworkFetcherFactory())
                    }
                    .build()
            }
            KoinApplication(configuration = koinConfiguration<RebornAppKoin>()) {
                RebornTheme {
                    RegistStoreScreen.invoke()
                }
            }
        }
    }
}