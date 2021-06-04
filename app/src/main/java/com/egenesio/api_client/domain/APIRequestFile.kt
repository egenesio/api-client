package com.egenesio.api_client.domain

import io.ktor.http.*
import java.io.File

/**
 * TODO doc
 */
data class APIRequestFile(
    val key: String,
    val file: File,
    val headers: Headers) {

    /**
     * TODO doc
     */
    constructor(file: File, contentType: String): this(file.name, file, Headers.build {
        append(HttpHeaders.ContentType, contentType)
        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
    })

}