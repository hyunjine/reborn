package com.hyunjine.reborn.config

import com.hyunjine.reborn.data.ApiResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.core.ResolvableType
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.util.MimeType

/**
 * [ApiResponse] sealed interface의 polymorphic type discriminator를 포함하여 직렬화하는 커스텀 인코더.
 *
 * Spring의 기본 [KotlinSerializationJsonEncoder]는 런타임 타입의 serializer를 사용하여
 * sealed interface의 type discriminator가 누락됩니다.
 * 이 인코더는 선언된 반환 타입에서 제네릭 인자를 추출하고,
 * [ApiResponse.serializer]를 통해 sealed class serializer를 강제로 사용합니다.
 *
 * @param json kotlinx serialization Json 인스턴스
 */
class PolymorphicKotlinSerializationEncoder(
    private val json: Json
) : KotlinSerializationJsonEncoder(json) {

    override fun canEncode(elementType: ResolvableType, mimeType: MimeType?): Boolean {
        if (ApiResponse::class.java.isAssignableFrom(elementType.toClass())) return true
        return super.canEncode(elementType, mimeType)
    }

    override fun encodeValue(
        value: Any,
        bufferFactory: DataBufferFactory,
        valueType: ResolvableType,
        mimeType: MimeType?,
        hints: MutableMap<String, Any>?
    ): DataBuffer {
        if (value is ApiResponse<*>) {
            val dataType = valueType.getGeneric(0)
            if (dataType != ResolvableType.NONE) {

                val dataSerializer = serializer(dataType.type)
                val apiResponseSerializer = ApiResponse.serializer(dataSerializer)
                @Suppress("UNCHECKED_CAST")
                val jsonString = json.encodeToString(apiResponseSerializer as KSerializer<Any>, value)
                return bufferFactory.wrap(jsonString.toByteArray(Charsets.UTF_8))
            }
        }
        return super.encodeValue(value, bufferFactory, valueType, mimeType, hints)
    }
}