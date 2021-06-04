package com.egenesio.api_client.domain

import com.egenesio.api_client.json.toMultipartData
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import java.io.File

/**
 * TODO
 */
class APIRequestBuilder {
    var path: String = ""

    var method: HttpMethod = HttpMethod.Get
    var apiVersion: APIVersion? = null
    var isPrivate: Boolean = false
    var responseKey: String? = null

    private var headersBuilder: HeadersBuilder = HeadersBuilder()
    private var pathParamsBuilder: ParameterBuilder = ParameterBuilder()
    private var queryParamsBuilder: ParameterBuilder = ParameterBuilder()

    var body: Any? = null
    var rawBody: JsonElement? = null

    private var filesBuilder: FilesBuilder = FilesBuilder()
    var multiPartData: List<PartData> = listOf()

    /**
     * TODO
     */
    class ParameterBuilder(from: Map<String, Any>? = null) {
        private val paramsMap: MutableMap<String, Any> = from?.toMutableMap() ?: mutableMapOf()

        infix fun String.to(value: Any) {
            paramsMap[this] = value
        }

        fun build(): Map<String,Any> = paramsMap.toMap()
    }

    /**
     * TODO
     */
    class FilesBuilder(from: List<APIRequestFile>? = null) {
        private val files: MutableList<APIRequestFile> = from?.toMutableList() ?: mutableListOf()

        fun append(file: File, contentType: String) {
            files.add(APIRequestFile(file, contentType))
        }

        fun append(key: String, file: File, headers: Headers) {
            files.add(APIRequestFile(key, file, headers))
        }

        fun append(file: APIRequestFile) {
            files.add(file)
        }

        fun build(): List<PartData> = formData {
            files.forEach { (key, file, headers) ->
                append(key, file.readBytes(), headers)
            }
        }
    }


    /**
     * TODO
     */
    fun build() = APIRequest(
        path = this.path,
        httpMethod = this.method,
        apiVersion = this.apiVersion,
        isPrivate = this.isPrivate,
        responseKey = this.responseKey,
        pathParams = this.pathParamsBuilder.build(),
        queryParams = this.queryParamsBuilder.build(),
        requestHeaders = this.headersBuilder.build(),
        body = this.body,
        rawBody = this.rawBody,
        multiPartData =  this.multiPartData + this.filesBuilder.build(),
        )

    /**
     * TODO
     */
    fun from(apiRequest: APIRequest) {
        path = apiRequest.path

        method = apiRequest.httpMethod
        apiVersion = apiRequest.apiVersion
        isPrivate = apiRequest.isPrivate
        responseKey = apiRequest.responseKey

        pathParamsBuilder = ParameterBuilder(apiRequest.pathParams)
        queryParamsBuilder = ParameterBuilder(apiRequest.queryParams)

        headersBuilder = HeadersBuilder().apply { appendAll(apiRequest.requestHeaders) }

        body = apiRequest.body
        rawBody = apiRequest.rawBody

        multiPartData = apiRequest.multiPartData
    }


    /**
     * TODO
     */
    fun pathParams(block: ParameterBuilder.() -> Unit) = pathParamsBuilder.apply(block)

    /**
     * TODO
     */
    fun queryParams(block: ParameterBuilder.() -> Unit) = queryParamsBuilder.apply(block)

    /**
     * TODO
     */
    fun headers(block: HeadersBuilder.() -> Unit) = headersBuilder.apply(block)



    /**
     * TODO
     */
    fun body(block: JsonObjectBuilder.() -> Unit) { rawBody = buildJsonObject(block) }



    /**
     * TODO
     */
    fun files(block: FilesBuilder.() -> Unit) = filesBuilder.apply(block)


    // MultiPart data

    /**
     * TODO
     */
    fun multiPartForm(block: FormBuilder.() -> Unit) {
        multiPartData = formData(block)
    }

    /**
     * TODO
     */
    fun multiPartBody(block: JsonObjectBuilder.() -> Unit) {
        multiPartData = buildJsonObject(block).toMultipartData()
    }

    /**
     * TODO
     */
    fun multiPartBody(body: JsonElement) {
        multiPartData = body.toMultipartData()
    }

    /**
     * TODO
     */
    inline fun <reified T> multiPartBody(body: T, withSerializer: Json) {
        try {
            multiPartData = withSerializer.encodeToJsonElement(body).toMultipartData()
        } catch (e: SerializationException) {
            e.printStackTrace()
        }
    }
}

/**
 * TODO
 */
public fun ApiRequest(block: APIRequestBuilder.() -> Unit): APIRequest {
    val builder = APIRequestBuilder()
    builder.apply(block)
    return builder.build()
}
