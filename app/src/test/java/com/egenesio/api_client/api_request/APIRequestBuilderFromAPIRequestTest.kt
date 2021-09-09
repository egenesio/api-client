package com.egenesio.api_client.api_request

import com.egenesio.api_client.domain.*
import com.egenesio.api_client.util.get
import io.ktor.http.*
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
class APIRequestBuilderFromAPIRequestTest {

    // TODO test files

    val _path = "overrided_path"
    private val _host = "this_is_the_host"
    private val _protocol = URLProtocol.HTTP

    object RequestPath: APIRequest() {
        override val requestHost: String = "this_is_the_host"
        override val requestProtocol: URLProtocol = URLProtocol.HTTP
        override val path = "overrided_path"
    }

    @Test
    fun requesBuilderFrom_rawBody() {
        val request = ApiRequest {
            from(RequestPath)
            body {
                put("string", "this_is_the_string")
                put("number", 2)
                put("bool", true)
            }
        }

        println(request)

        assertEquals(_host, request.requestHost)
        assertEquals(_protocol, request.requestProtocol)
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

}