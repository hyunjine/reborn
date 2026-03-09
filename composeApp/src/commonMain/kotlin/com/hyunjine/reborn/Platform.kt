package com.hyunjine.reborn

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform