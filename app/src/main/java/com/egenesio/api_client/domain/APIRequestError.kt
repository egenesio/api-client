package com.egenesio.api_client.domain

import io.ktor.client.features.*

/**
 * TODO
 */
sealed class APIRequestError: APIError {

    data class Unexpected(val reason: String): APIRequestError()
    data class Network(val reason: String): APIRequestError()
    data class Http(val body: String, val status: Int, val e: ResponseException): APIRequestError()
    data class Serialization(val reason: String): APIRequestError()

    override val message: String get() = when(this) {
        is Http -> body
        is Network -> reason
        is Serialization -> reason
        is Unexpected -> reason
    }
}