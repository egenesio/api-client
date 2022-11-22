package com.egenesio.api_client.networking

import com.egenesio.api_client.domain.APIClientLogLevel
import com.egenesio.api_client.domain.APIErrorInterceptor
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

/**
 * TODO add
 */
class APIClientBuilder {

    var logLevel = APIClientLogLevel.None

    var ktorClient: HttpClient? = null
    var errorInterceptor: APIErrorInterceptor? = null
    var jsonFormatter: Json? = null

    private val headers = HeadersBuilder()
    private var auth: (Auth.() -> Unit)? = null
    private var url: URLBuilder.(URLBuilder) -> Unit = {}
    private var timeoutContiguration: (HttpTimeout.HttpTimeoutCapabilityConfiguration.() -> Unit)? = null

    /**
     * TODO add
     */
    private val json by lazy { jsonFormatter ?: defaultJsonFormatter }

    /**
     * TODO add
     */
    private val client by lazy { ktorClient ?: jsonKtorClient }

    /**
     * TODO add
     */
    fun build() = APIClient(
        ktorClient = client,
        jsonFormatter = json,
        errorInterceptor = errorInterceptor,
        logLevel = logLevel
    )

    public fun auth(block: Auth.() -> Unit) {
        auth = block
    }

    public fun url(block: URLBuilder.(URLBuilder) -> Unit) {
        url = block
    }

    public fun headers(block: HeadersBuilder.() -> Unit) = headers.apply(block)

    public fun timeout(block: HttpTimeout.HttpTimeoutCapabilityConfiguration.() -> Unit) {
        this.timeoutContiguration = block
    }

    private val clientLogger by lazy {
        object : Logger {
            override fun log(message: String) = println(message)
        }
    }

    private val defaultJsonFormatter by lazy {
        Json {}
    }

    /**
     * TODO add
     */
    private val jsonKtorClient by lazy {
        val authBlock = auth
        val urlBlock = url
        val baseHeaders = headers.build()

        HttpClient(CIO) {
            install(Logging) {
                logger = clientLogger
                level = logLevel.ktorLogLevel
            }

            timeoutContiguration?.let { configuration ->
                install(HttpTimeout, configure = configuration)
            }

            if (authBlock != null) {
                install(Auth, authBlock)
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }

            defaultRequest {
                url(urlBlock)

                headers {
                    appendAll(baseHeaders)
                }
                // Content Type
                accept(ContentType.Application.Json)
            }
        }
    }
}

/**
 * TODO add
 */
fun ApiClient(block: APIClientBuilder.() -> Unit): APIClient {
    val builder = APIClientBuilder()
    builder.apply(block)
    return builder.build()
}