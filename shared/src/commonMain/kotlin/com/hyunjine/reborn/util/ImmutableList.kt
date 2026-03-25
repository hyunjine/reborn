package com.hyunjine.reborn.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

fun<T> ImmutableList(
    size: Int, init: (index: Int) -> T
): ImmutableList<T> {
    return List(size = size, init = init).toImmutableList()
}

inline fun <T, R> ImmutableList<T>.mapStable(transform: (T) -> R): ImmutableList<R> {
    return map(transform).toImmutableList()
}

class ImmutableListSerializer<T>(
    dataSerializer: KSerializer<T>
) : KSerializer<ImmutableList<T>> {

    private val delegateSerializer = ListSerializer(dataSerializer)

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        encoder.encodeSerializableValue(delegateSerializer, value.toList())
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> {
        return decoder.decodeSerializableValue(delegateSerializer).toImmutableList()
    }
}