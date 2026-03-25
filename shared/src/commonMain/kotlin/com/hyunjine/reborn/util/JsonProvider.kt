package com.hyunjine.reborn.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * 클라이언트와 서버에서 공통으로 사용하는 [Json] 인스턴스.
 */
val DefaultJson: Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = true
    serializersModule = SerializersModule {
        contextual(ImmutableList::class) { args -> ImmutableListSerializer(args[0]) }
    }
}
