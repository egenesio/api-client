package com.egenesio.api_client.api_request

import com.egenesio.api_client.domain.*
import com.egenesio.api_client.util.get
import io.ktor.http.*
import kotlinx.serialization.Serializable
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
class APIRequestBuilderTest {

    // TODO test files

    private val _host = "this_is_the_host"
    private val _protocol = URLProtocol.HTTP
    private val _path = "this_is_the_path"

    @Test
    fun `requesBuilder custom host`() {
        val request = ApiRequest {
            host = _host
            protocol = _protocol
        }

        println(request)

        assertEquals(_host, request.requestHost)
        assertEquals(_protocol, request.requestProtocol)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun requesBuilder_path() {
        val request = ApiRequest {
            path = _path
        }

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun requesBuilder_responseKey() {
        val key = "data"

        val request = ApiRequest {
            path = _path
            responseKey = key
        }

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)

        assertEquals(key, request.responseKey)
    }

    @Test
    fun requesBuilder_method() {
        val request = ApiRequest {
            path = _path
            method = HttpMethod.Post
        }

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Post, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun requesBuilder_apiVersion() {
        val request = ApiRequest {
            path = _path
            apiVersion = V1ApiVersion
        }

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertEquals(V1ApiVersion.encoded(), request.apiVersion!!.encoded())
        assertEquals(false, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun requesBuilder_isPrivate() {
        val request = ApiRequest {
            path = _path
            isPrivate = true
        }

        println(request)

        assertEquals(_path, request.path)
        assertEquals(HttpMethod.Get, request.httpMethod)
        assertNull(request.apiVersion)
        assertEquals(true, request.isPrivate)
        assertEquals(mapOf<String,Any>(), request.pathParams)
        assertEquals(mapOf<String,Any>(), request.queryParams)
        assertNull(request.body)
        assertNull(request.rawBody)
    }

    @Test
    fun requesBuilder_pathParams() {
        val request = ApiRequest {
            path = _path
            pathParams {
                "path_1" to "value_1"
            }
        }

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
    fun requesBuilder_queryParams() {
        val request = ApiRequest {
            path = _path
            queryParams {
                "order" to "asc"
            }
        }

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
    fun requesBuilder_headers() {
        val request = ApiRequest {
            path = _path
            headers {
                append("header_custom", "value_custom")
            }
        }

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
    fun requesBuilder_rawBody() {
        val request = ApiRequest {
            path = _path
            body {
                put("string", "this_is_the_string")
                put("number", 2)
                put("bool", true)
            }
        }

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

    @Serializable
    data class TestClass(
        val name: String = "this_is_the_name",
        val age: Int = 10
    )

    @Test
    fun requesBuilder_body() {
        val _path = "this_is_the_path"

        val request = ApiRequest {
            path = _path
            body = TestClass("Emilio", 33)
        }

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

    object V1ApiVersion: APIVersion {
        override val name: String = "V1"
    }

}