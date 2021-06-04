package com.egenesio.api_client.api_request

import com.egenesio.api_client.domain.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class APIRequestHttpRequestTest {

    // TODO test files

    private val _path = "endpoint/object/action"
    private val _pathWithParams = "endpoint/{objectId}/action/{subObjectId}"

    object V1API: APIVersion {
        override val name = "v1"
    }

    @Test
    fun apiRequest_httpRequest() {
        val objectId = "dsafa2asd23ds"
        val subObjectId = "nj34jjdasjbi243"

        val request = ApiRequest {
            path = _pathWithParams
            method = HttpMethod.Post
            apiVersion = APIRequestPathTest.V1API
            pathParams {
                "objectId" to objectId
                "subObjectId" to subObjectId
            }
            queryParams {
                "order" to "asc"
                "limit" to 20
                "unique" to false
            }

        }


        val requestData = request.httpRequest().build()

        println(request)
        println(requestData)

        assertEquals(request.requestPath, requestData.url.encodedPath)
        assertEquals(request.httpMethod, requestData.method)
        assertTrue(requestData.url.encodedPath.contains(V1API.encoded()))

        assertTrue(requestData.url.toString().contains("order=asc"))
        assertTrue(requestData.url.toString().contains("limit=20"))
        assertTrue(requestData.url.toString().contains("unique=false"))

        // TODO assert token
    }

    @Test
    fun apiRequest_httpRequest_headers() {
        val request = ApiRequest {
            path = _path
            headers {
                append("header_custom", "value_custom")
                append("header_custom_2", "123")
                append("header_custom_3", "true")
            }
        }

        val headers = HeadersBuilder().apply {
            append("header_custom", "value_custom")
            append("header_custom_2", "123")
            append("header_custom_3", "true")
        }.build()

        val requestData = request.httpRequest().build()

        println(request)
        println(requestData)
        println(requestData.headers)

        assertEquals(headers, requestData.headers)
    }

    /**
     * Seems like we cannot test the bodies directly here
     * We have to cretae request and validates the body from the HttpClient
     */

//    @Test
//    fun apiRequest_httpRequest_rawBody() {
//        val request = apiRequest {
//            path = _path
//            jsonBody {
//                put("string", "this_is_the_string")
//                put("number", 2)
//                put("bool", true)
//            }
//        }
//
//        val requestData = request.httpRequest().build()
//
//        println(request)
//        println(requestData)
//        println(requestData.body)
//
////        assertEquals(headers, requestData.headers)
//    }
//
//    @Serializable
//    data class TestClass(
//        val name: String = "this_is_the_name",
//        val age: Int = 10
//    )
//
//    @Test
//    fun apiRequest_httpRequest_body() {
//        val request = apiRequest {
//            path = _path
//            body = TestClass("Emilio", 33)
//        }
//
//        val requestData = request.httpRequest().build()
//
//        println(request)
//        println(requestData)
//        println(requestData.body)
//    }

}