package com.hyunjine.reborn

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class RebornApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        KakaoMapSdk.init(this, BuildConfig.KAKAO_MAP_API_KEY)
    }
}