package com.egenesio.api_client.util

import io.ktor.client.request.forms.*

fun FormBuilder.append(key: String, value: String?) {
    value?.let { append(key, it) }
}

fun FormBuilder.append(key: String, value: Number?) {
    value?.let { append(key, it) }
}