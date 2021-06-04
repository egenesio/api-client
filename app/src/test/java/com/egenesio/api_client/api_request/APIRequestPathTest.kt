package com.egenesio.api_client.api_request

import com.egenesio.api_client.domain.APIVersion
import com.egenesio.api_client.domain.ApiRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class APIRequestPathTest {

    // TODO test files

    private val _path = "endpoint/object/action"
    private val _pathWithParams = "endpoint/{objectId}/action/{subObjectId}"

    object V1API: APIVersion {
        override val name = "v1"
    }

    @Test
    fun apiRequest_path_clean() {

        val request = ApiRequest {
            path = _path
        }

        println(request)
        println(request.requestPath)

        val path = _path
        assertEquals(path, request.requestPath)
    }

    @Test
    fun apiRequest_path_version() {

        val request = ApiRequest {
            path = _path
            apiVersion = V1API
        }

        println(request)
        println(request.requestPath)

        val path = V1API.encoded() + _path
        assertEquals(path, request.requestPath)
    }

    @Test
    fun apiRequest_path_params_string() {
        val objectId = "dsafa2asd23ds"
        val subObjectId = "nj34jjdasjbi243"

        val request = ApiRequest {
            path = _pathWithParams
            pathParams {
                "objectId" to objectId
                "subObjectId" to subObjectId
            }
        }

        println(request)
        println(request.requestPath)

        val path = "endpoint/dsafa2asd23ds/action/nj34jjdasjbi243"
        assertEquals(path, request.requestPath)
    }

    @Test
    fun apiRequest_path_params_number() {
        val objectId = 123
        val subObjectId = 456

        val request = ApiRequest {
            path = _pathWithParams
            pathParams {
                "objectId" to objectId
                "subObjectId" to subObjectId
            }
        }

        println(request)
        println(request.requestPath)

        val path = "endpoint/123/action/456"
        assertEquals(path, request.requestPath)
    }

    @Test
    fun apiRequest_path_params_bool() {
        val objectId = true
        val subObjectId = false

        val request = ApiRequest {
            path = _pathWithParams
            pathParams {
                "objectId" to objectId
                "subObjectId" to subObjectId
            }
        }

        println(request)
        println(request.requestPath)

        val path = "endpoint/true/action/false"
        assertEquals(path, request.requestPath)
    }

    @Test
    fun apiRequest_path_params_version() {
        val objectId = "dsafa2asd23ds"
        val subObjectId = "nj34jjdasjbi243"

        val request = ApiRequest {
            path = _pathWithParams
            apiVersion = V1API
            pathParams {
                "objectId" to objectId
                "subObjectId" to subObjectId
            }
        }

        println(request)
        println(request.requestPath)

        val path = "v1/endpoint/dsafa2asd23ds/action/nj34jjdasjbi243"
        assertEquals(path, request.requestPath)
    }
}