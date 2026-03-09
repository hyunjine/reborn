package com.hyunjine.reborn

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hyunjine.reborn.core.di.appModule
import com.hyunjine.reborn.home.HomeScreen
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext

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
                HomeScreen()
            }
        }
    }
}
