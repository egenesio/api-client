package com.egenesio.api_client.networking

import com.egenesio.api_client.domain.*
import com.egenesio.api_client.json.decodeFrom
import com.egenesio.api_client.json.get
import com.egenesio.api_client.json.jsonElementFromBody
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

class APIClient(
    val ktorClient: HttpClient,
    val jsonFormatter: Json,
    val logLevel: APIClientLogLevel,
    val errorInterceptor: APIErrorInterceptor? = null) {

    /**
     * TODO
     */
    suspend fun request(apiRequest: APIRequest): APIResponse<JsonElement> = withContext(Dispatchers.IO) {
        try {
            val requestResponse: String = ktorClient.request(apiRequest.httpRequest())

            ensureActive()

            val json = jsonFormatter.jsonElementFromBody(requestResponse, apiRequest.responseKey)

            return@withContext APIResponse.Success(json, json)

        } catch (e: Exception) {
            ensureActive()

            val error = handleException(e)
            return@withContext APIResponse.Error(errorInterceptor?.from(error) ?: error)
        }
    }

    /**
     * TODO
     */
    suspend inline fun <reified T> requestItem(apiRequest: APIRequest): APIResponse<T> = withContext(Dispatchers.IO) {
        try {
            val requestResponse: String = ktorClient.request(apiRequest.httpRequest())

            ensureActive()

            val json = jsonFormatter.jsonElementFromBody(requestResponse, apiRequest.responseKey)
            val obj = jsonFormatter.decodeFrom<T>(json)

            return@withContext APIResponse.Success(obj, json)

        } catch (e: Exception) {
            ensureActive()

            val error = handleException(e)
            return@withContext APIResponse.Error(errorInterceptor?.from(error) ?: error)
        }
    }

    /**
     * TODO add
     */
    suspend fun handleException(e: Exception): APIRequestError {
        if (logLevel.isLogging) {
            e.printStackTrace()
            println(e.message)
        }

        return try {
            when(e) {
                is ResponseException -> {
                    val response = e.response

                    APIRequestError.Http(
                        body = response.readText(),
                        status = response.status.value,
                        e = e)
                }

                is SerializationException -> {
                    APIRequestError.Serialization(e.message ?: e.toString())
                }

                else -> {
                    APIRequestError.Network(e.message ?: e.toString())
                }
            }

        } catch (e: Exception) {
            if (logLevel.isLogging) {
                e.printStackTrace()
            }

            APIRequestError.Unexpected(e.message ?: e.toString())
        }
    }

    /**
     * TODO
     */
    suspend inline fun <reified T> requestItems(apiRequest: APIRequest): APIResponse<List<T>> = requestItem(apiRequest)

}

/**
 * TODO add
 */
suspend fun APIClient.request(block: APIRequestBuilder.() -> Unit): APIResponse<JsonElement> = request(ApiRequest(block))

/**
 * TODO add
 */
suspend inline fun <reified T> APIClient.requestItem(noinline block: APIRequestBuilder.() -> Unit): APIResponse<T> = requestItem(ApiRequest(block))

/**
 * TODO add
 */
suspend inline fun <reified T> APIClient.requestItems(noinline block: APIRequestBuilder.() -> Unit): APIResponse<List<T>> = requestItem(block)