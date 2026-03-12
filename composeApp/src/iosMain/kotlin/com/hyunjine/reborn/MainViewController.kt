package com.hyunjine.reborn

import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun MainViewController(): androidx.compose.ui.window.UIViewController {
    Napier.base(DebugAntilog())
    return ComposeUIViewController { RebornApp() }
}