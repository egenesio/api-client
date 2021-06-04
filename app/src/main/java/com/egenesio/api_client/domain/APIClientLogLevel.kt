package com.egenesio.api_client.domain

import io.ktor.client.features.logging.*

/**
 * TODO
 */
enum class APIClientLogLevel {
    All,
    Hedears,
    Body,
    Info,
    None;

    val ktorLogLevel: LogLevel
        get() = when (this) {
        All -> LogLevel.ALL
        Hedears -> LogLevel.HEADERS
        Body -> LogLevel.BODY
        Info -> LogLevel.INFO
        None -> LogLevel.NONE
    }

    val isLogging: Boolean get() = when (this) {
        None -> false
        else -> true
    }
}