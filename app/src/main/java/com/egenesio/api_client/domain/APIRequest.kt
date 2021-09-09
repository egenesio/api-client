package com.egenesio.api_client.domain

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlinx.serialization.json.*

/**
 * TODO
 */
open class APIRequest(
    open val requestHost: String? = null,
    open val requestProtocol: URLProtocol = URLProtocol.HTTPS,
    open val path: String = "",
    open val httpMethod: HttpMethod = HttpMethod.Get,
    open val apiVersion: APIVersion? = null,
    open val isPrivate: Boolean = false,
    open val responseKey: String? = null,

    open val pathParams: Map<String, Any> = mapOf(),
    open val queryParams: Map<String, Any> = mapOf(),
    open val requestHeaders: Headers = Headers.Empty,

    open val body: Any? = null,
    open val rawBody: JsonElement? = null,

    open val multiPartData: List<PartData> = listOf()
    ) {

    // TODO add support for attributes

    companion object {
        /**
         * TODO
         */
        val httpRequestAttributeIsPrivate = AttributeKey<Boolean>("isPrivate")
    }

    /**
     * TODO
     */
    open val requestPath: String by lazy {
        val apiVersion = apiVersion?.encoded() ?: ""

        val path = pathParams.entries.fold(path) { path, (key, value) ->
            path.replace("{$key}", value.toString())
        }

        return@lazy apiVersion + path
    }

    /**
     * TODO doc
     */
    open val requestType: APIRequestType by lazy {
        if (multiPartData.isNotEmpty()) APIRequestType.MultiPart else APIRequestType.Json
    }

    /**
     * TODO
     */
    open val jsonBody: Any? by lazy { rawBody ?: body }

    /**
     * TODO
     */
    open fun httpRequest(): HttpRequestBuilder = HttpRequestBuilder().apply {
        // if the request has a custom host
        requestHost?.let { requestHost ->
            url {
                host = requestHost
                protocol = requestProtocol
                encodedPath = requestPath
            }

        // if the request does not have a custom host, then use defualt one
        } ?: run {
            url {
                encodedPath = requestPath
            }
        }

        method = httpMethod
        queryParams.forEach { param -> parameter(param.key, param.value) }

        setAttributes {
            put(httpRequestAttributeIsPrivate, isPrivate)
        }

        headers {
            appendAll(requestHeaders)
        }

        when(requestType) {
            APIRequestType.Json -> {
                jsonBody?.let {
                    contentType(ContentType.Application.Json)
                    body = it
                }
            }
            APIRequestType.MultiPart -> {
                body = MultiPartFormDataContent(multiPartData)
            }
        }
    }

    override fun toString(): String {
        val string = "path: [$path], httpMethod: [${httpMethod.value}], isPrivate: [$isPrivate]"
        val apiVersion = apiVersion?.let { ", apiVersion: [${it.name}]" } ?: ""
        val responseKey = responseKey?.let { ", responseKey: [$it]" } ?: ""
        val pathParams = if(pathParams.isNotEmpty()) ", pathParams: [$pathParams]" else ""
        val queryParams = if(queryParams.isNotEmpty()) ", queryParams: [$queryParams]" else ""
        val headers = if (!requestHeaders.isEmpty()) ", headers: [$requestHeaders]" else ""
        val body = body?.let { ", body: [$it]" } ?: ""
        val rawBody = rawBody?.let { ", rawBody: [$it]" } ?: ""

        // TODO formData

        return string + apiVersion + responseKey + pathParams + queryParams + headers + body + rawBody
    }
}