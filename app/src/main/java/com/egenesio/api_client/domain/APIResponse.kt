package com.egenesio.api_client.domain

import kotlinx.serialization.json.JsonElement

/**
 * TODO
 */
sealed class APIResponse<out T> {

    data class Success<T>(val data: T, val raw: JsonElement): APIResponse<T>()
    data class Error(val error: APIError): APIResponse<Nothing>()

}