package com.hyunjine.reborn

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class RebornApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        appContext = this
    }

    companion object {
        lateinit var appContext: Application
            private set
    }
}