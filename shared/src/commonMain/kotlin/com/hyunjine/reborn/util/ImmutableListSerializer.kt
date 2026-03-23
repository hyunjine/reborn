package com.hyunjine.reborn.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

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

val immutableListSerializerModule = SerializersModule {
    // ImmutableList 인터페이스에 대해 ImmutableListSerializer를 사용하도록 등록
    contextual(ImmutableList::class) { args ->
        ImmutableListSerializer(args[0])
    }
}