package com.egenesio.api_client.api_request

import com.egenesio.api_client.domain.*
import com.egenesio.api_client.networking.ApiClient
import com.egenesio.api_client.util.get
import io.ktor.client.features.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class APIRequestInheritanceTest {

    // TODO test files
    // TODO test requestType
    // TODO formData

    val _path = "overrided_path"

    object RequestPath: APIRequest() {
        override val path = "overrided_path"
    }

    object RequestKey: APIRequest() {
        override val path = "overrided_path"
        override val responseKey = "data"
    }

    object RequestMethod: APIRequest() {
        override val path = "overrided_path"
        override val httpMethod: HttpMethod get() = HttpMethod.Post
    }

    object RequestApiVersion: APIRequest() {
        override val path = "overrided_path"
        override val apiVersion = V1ApiVersion
    }

    object RequestPrivate: APIRequest() {
        override val path = "overrided_path"
        override val isPrivate = true
    }

    class RequestPathParams(override val pathParams: Map<String,Any>): APIRequest() {
        override val path = "overrided_path"
    }

    object RequestPathParamsOb: APIRequest() {
        override val path = "overrided_path"
        override val pathParams= mapOf(
            "path_1" to "value_1"
        )
    }

    class RequestQueryParams(override val queryParams: Map<String,Any>): APIRequest() {
        override val path = "overrided_path"
    }

    object RequestQueryParamsOb: APIRequest() {
        override val path = "overrided_path"
        override val queryParams = mapOf(
            "order" to "asc"
        )
    }

    class RequestHeaders(override val requestHeaders: Headers): APIRequest() {
        override val path = "overrided_path"
    }

    object RequestHeadersOb: APIRequest() {
        override val path = "overrided_path"
        override val requestHeaders: Headers = HeadersBuilder().apply {
            append("header_custom", "value_custom")
        }.build()
    }

    class RequestRawBody(override val rawBody: JsonElement?): APIRequest() {
        override val path = "overrided_path"
    }

    object RequestRawBodyOb: APIRequest() {
        override val path = "overrided_path"
        override val rawBody: JsonElement = buildJsonObject {
            put("string", "this_is_the_string")
            put("number", 2)
            put("bool", true)
        }
    }

    class RequestBody(override val body: Any?): APIRequest() {
        override val path = "overrided_path"
    }

    object RequestBodyOb: APIRequest() {
        override val path = "overrided_path"
        override val body: Any = TestClass("Emilio", 33)
    }

    @Test
    fun inheritance_request_path() {
        val request = RequestPath

        println(request)

        assertEquals(_path, RequestPath.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_responseKey() {
        val request = RequestKey

        println(request)

        assertEquals(_path, RequestKey.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)

        assertEquals("data", RequestKey.responseKey)
    }

    @Test
    fun inheritance_request_method() {
        val request = RequestMethod

        println(request)

        assertEquals(_path, RequestMethod.path)
        assertEquals(HttpMethod.Post, RequestMethod.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_apiVersion() {
        val request = RequestApiVersion

        println(request)

        assertEquals(_path, RequestApiVersion.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertEquals(V1ApiVersion.encoded(), RequestApiVersion.apiVersion!!.encoded())
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_isPrivate() {
        val request = RequestPrivate

        println(request)

        assertEquals(_path, RequestPrivate.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(true, RequestPrivate.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_pathParams() {

        val request = RequestPathParams(mapOf(
            "path_1" to "value_1"
        ))

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(
            "path_1" to "value_1"
        ), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_pathParams_ob() {

        val request = RequestPathParamsOb

        println(request)

        assertEquals(_path, RequestPathParamsOb.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(
            "path_1" to "value_1"
        ), RequestPathParamsOb.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_queryParams() {
        val request = RequestQueryParams(mapOf(
            "order" to "asc"
        ))

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(
            "order" to "asc"
        ), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_queryParams_ob() {
        val request = RequestQueryParamsOb

        println(request)

        assertEquals(_path, RequestQueryParamsOb.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(
            "order" to "asc"
        ), RequestQueryParamsOb.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_headers() {
        val request = RequestHeaders(HeadersBuilder().apply {
            append("header_custom", "value_custom")
        }.build())

        val headers = HeadersBuilder().apply {
            append("header_custom", "value_custom")
        }.build()

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertEquals(headers, request.requestHeaders)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_headers_ob() {
        val request = RequestHeadersOb

        val headers = HeadersBuilder().apply {
            append("header_custom", "value_custom")
        }.build()

        println(request)

        assertEquals(_path, RequestHeadersOb.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertEquals(headers, request.requestHeaders)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun inheritance_request_rawBody() {
        val request = RequestRawBody(buildJsonObject {
            put("string", "this_is_the_string")
            put("number", 2)
            put("bool", true)
        })

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNotNull(request.rawBody)

        val body = request.rawBody!!
        val number: Int? = body["number"]
        val string: String? = body["string"]
        val bool: Boolean? = body["bool"]

        assertEquals(2, number)
        assertEquals("this_is_the_string", string)
        assertEquals(true, bool)
    }

    @Test
    fun inheritance_request_rawBody_ob() {
        val request = RequestRawBodyOb

        println(request)

        assertEquals(_path, RequestRawBodyOb.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNotNull(RequestRawBodyOb.rawBody)

        val body = RequestRawBodyOb.rawBody!!
        val number: Int? = body["number"]
        val string: String? = body["string"]
        val bool: Boolean? = body["bool"]

        assertEquals(2, number)
        assertEquals("this_is_the_string", string)
        assertEquals(true, bool)
    }

    @Serializable
    data class TestClass(
        val name: String = "this_is_the_name",
        val age: Int = 10
    )

    @Test
    fun inheritance_request_body() {
        val request = RequestBody(TestClass("Emilio", 33))

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNotNull(request.body)
        assertNull(request.rawBody)

        val body = request.body as TestClass

        assertEquals("Emilio", body.name)
        assertEquals(33, body.age)
    }

    @Test
    fun inheritance_request_body_ob() {
        val request = RequestBodyOb

        println(request)

        assertEquals(_path, RequestBodyOb.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNotNull(RequestBodyOb.body)
        assertNull(request.rawBody)

        val body = RequestBodyOb.body as TestClass

        assertEquals("Emilio", body.name)
        assertEquals(33, body.age)
    }

    object V1ApiVersion: APIVersion {
        override val name: String = "V1"
    }

}