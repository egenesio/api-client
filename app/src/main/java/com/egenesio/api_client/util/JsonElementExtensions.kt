package com.egenesio.api_client.util

import io.ktor.client.request.forms.*
import io.ktor.http.content.*
import kotlinx.serialization.json.*

/**
 * TODO add
 */
inline operator fun <reified T> JsonElement.get(key: String): T? {
    return when(T::class) {
        // Json classes
        JsonElement::class -> this.jsonObject[key] as? T
        JsonArray::class -> this.jsonObject[key]?.jsonArray as? T
        JsonObject::class -> this.jsonObject[key] as? T
        JsonPrimitive::class -> this.jsonObject[key]?.jsonPrimitive as? T

        // Primitives classes
        String::class -> this.jsonObject[key]?.jsonPrimitive?.contentOrNull as? T
        Boolean::class -> this.jsonObject[key]?.jsonPrimitive?.booleanOrNull as? T

        Long::class -> this.jsonObject[key]?.jsonPrimitive?.longOrNull as? T
        Int::class -> this.jsonObject[key]?.jsonPrimitive?.intOrNull as? T
        Double::class -> this.jsonObject[key]?.jsonPrimitive?.doubleOrNull as? T
        Float::class -> this.jsonObject[key]?.jsonPrimitive?.floatOrNull as? T

        else -> return null
    }
}

/**
 * TODO add
 */
val JsonObject.elem: JsonElement
    get() = this

/**
 * TODO add
 */
fun JsonElement.toMultipartData(): List<PartData> {
    val json = this
    return formData {
        when(json) {
            is JsonObject -> {

                for ((key, value) in json.entries) {
                    when(value) {
                        is JsonPrimitive -> {
                            append(key, value.jsonPrimitive.content)
                        }
                        else -> {
                            append(key, value.toString())
                        }
                    }
                }
            }
            is JsonArray -> {
                // do nothing because we need a key to append the body
                // so we will need to pass a jsonObject and the jsonArray inside
            }
            // TODO add support for others?
            else -> {}
        }
    }
}