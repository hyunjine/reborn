package com.hyunjine.reborn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavEntry
import androidx.navigation3.ui.rememberNavBackStack
import com.hyunjine.reborn.core.di.appModule
import com.hyunjine.reborn.core.navigation.AppNavKey
import com.hyunjine.reborn.home.HomeScreen
import com.hyunjine.reborn.store.RegistStoreScreen
import androidx.compose.ui.tooling.preview.Preview
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
                val backStack = rememberNavBackStack(initialKey = AppNavKey.Home as AppNavKey)

                NavDisplay(
                    backStack = backStack,
                    onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
                    entryProvider = { key ->
                        when (key) {
                            is AppNavKey.Home -> NavEntry(key) {
                                HomeScreen(
                                    onCenterClick = { /* TODO: Detail */ },
                                    onNavClick = { route ->
                                        val nextKey = when (route) {
                                            "home" -> AppNavKey.Home
                                            "price" -> AppNavKey.Price
                                            "auction" -> AppNavKey.Auction
                                            "my" -> AppNavKey.MyInfo
                                            else -> null
                                        }
                                        nextKey?.let {
                                            if (backStack.last() != it) {
                                                backStack.clear()
                                                backStack.add(it)
                                            }
                                        }
                                    }
                                )
                            }
                            is AppNavKey.Price -> NavEntry(key) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("준비 중인 화면입니다: 시세")
                                }
                            }
                            is AppNavKey.Auction -> NavEntry(key) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("준비 중인 화면입니다: 역경매")
                                }
                            }
                            is AppNavKey.MyInfo -> NavEntry(key) {
                                RegistStoreScreen(onBack = { 
                                    backStack.clear()
                                    backStack.add(AppNavKey.Home) 
                                })
                            }
                        }
                    }
                )
            }
        }
    }
}
