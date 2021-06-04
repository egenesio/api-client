package com.egenesio.api_client.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Function that gets the [JsonElement] object from the given [String]
 * Can receive a [String] key to search for that particular element in the root element of the parsed [JsonElement]
 *
 * @param body A string representation of the Json
 * @param key An opcional key to ser for in the root element of the parsed [JsonElement]
 *
 * @return The parsed [JsonElement] or null
 */

/**
 * TODO add
 */
suspend fun Json.jsonElementFromBody(body: String, key: String? = null): JsonElement = withContext(Dispatchers.Default) {
    val jsonElement = parseToJsonElement(body)

    if (key == null) return@withContext jsonElement

    // if the key exists, then search for that element in the root of the tree
    return@withContext jsonElement[key] ?: throw SerializationException("The element with key [$key] does not exists in the root of the json")
}

/**
 * Function that gets the [JsonElement] object from the given [String]
 * Can receive a [String] key to search for that particular element in the root element of the parsed [JsonElement]
 *
 * @param body A string representation of the Json
 * @param key An opcional key to ser for in the root element of the parsed [JsonElement]
 *
 * @return The parsed [JsonElement] or null
 */
/**
 * TODO add
 */
suspend fun Json.jsonElementFromBodyOrNull(body: String?, key: String? = null): JsonElement? = withContext(Dispatchers.Default) {
    try {

        return@withContext body?.let { jsonElementFromBody(it, key) }

    } catch (e: Exception) {
        e.printStackTrace()
        // TODO check if log is enabled
        println(e.message)
        return@withContext null
    }
}

/**
 * TODO doc
 */
suspend inline fun <reified T> Json.decodeFrom(body: String, key: String? = null): T = withContext(Dispatchers.Default) {
    val json = jsonElementFromBody(body, key)

    return@withContext decodeFromJsonElement<T>(json)
}

/**
 * TODO doc
 */
suspend inline fun <reified T> Json.decodeNullableFrom(body: String?, key: String? = null): T? = withContext(Dispatchers.Default) {
    try {

        return@withContext body?.let { decodeFrom(it, key) }

    } catch (e: Exception) {
        e.printStackTrace()
        // TODO check if log is enabled
        println(e.message)
        return@withContext null
    }
}

/**
 * TODO doc
 */
suspend inline fun <reified T> Json.decodeFrom(json: JsonElement, key: String? = null): T = withContext(Dispatchers.Default) {
    val element = key?.let { json[it] } ?: json
    return@withContext decodeFromJsonElement<T>(element)
}

/**
 * TODO doc
 */
suspend inline fun <reified T> Json.decodeNullableFrom(json: JsonElement?, key: String? = null): T? = withContext(Dispatchers.Default) {
    try {

        return@withContext json?.let { decodeFrom(it, key) }

    } catch (e: Exception) {
        e.printStackTrace()
        // TODO check if log is enabled
        println(e.message)
        return@withContext null
    }
}