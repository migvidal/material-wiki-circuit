package com.migvidal.wikicircuit.core.api.common_model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

interface Page {
    val title: String
    val pageid: Int?
}

@Serializable(with = QuerySerializer::class)
data class Query<out T : Page>(
    val normalized: List<Normalized> = emptyList(),
    val pages: List<T> = emptyList(),
) {
    @Serializable
    data class Normalized(
        val fromencoded: Boolean? = null,
        val from: String? = null,
        val to: String? = null,
    )
}

class QuerySerializer<T : Page>(pageSerializer: KSerializer<T>) :
    KSerializer<Query<T>> {
    private val pagesSerializer = ListSerializer(pageSerializer)
    private val normalizedSerializer = ListSerializer(Query.Normalized.serializer())


    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("Query") {
            element("normalized", normalizedSerializer.descriptor)
            element("pages", pagesSerializer.descriptor)
        }

    override fun serialize(encoder: Encoder, value: Query<T>) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, normalizedSerializer, value.normalized)
            encodeSerializableElement(descriptor, 1, pagesSerializer, value.pages)
        }
    }

    override fun deserialize(decoder: Decoder): Query<T> = decoder.decodeStructure(descriptor) {
        var normalized: List<Query.Normalized> = emptyList()
        var pages: List<T> = emptyList()

        loop@ while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> normalized = decodeSerializableElement(descriptor, 0, normalizedSerializer)
                1 -> pages = decodeSerializableElement(descriptor, 1, pagesSerializer)
                else -> throw SerializationException("Unknown resultIndex $index")
            }
        }
        return@decodeStructure Query(normalized = normalized, pages = pages)
    }
}

@Serializable
data class ApiImage(
    val source: String, val height: Int, val width: Int,
)