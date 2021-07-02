package com.egenesio.api_client.util

import com.egenesio.api_client.domain.APIError
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ErrorMapperTest {

    private val validJsonWithData = """
        {"error":null,"data":{"version":{"updateAvailable":false,"mustUpdate":false,"currentVersionInfo":"","newVersionInfo":[],"latestVersion":"3.5.0"}},"warnings":[]}
    """.trimIndent()

    private val validJsonWithoutData = """
        {"version":{"updateAvailable":false,"mustUpdate":false,"currentVersionInfo":"","newVersionInfo":[],"latestVersion":"3.5.0"}}
    """.trimIndent()

    private val errorJsonWithoutMessages = """
        {"message":"Resource Not Found","error":{"status":404}}
    """.trimIndent()

    private val errorJsonWithMessages = """
        {"error":{"status":403,"devMessage":"Authentication credentials missing.","userMessage":"User Authentication error. Please try again."}}
    """.trimIndent()


    @Test
    fun test_deser() = runBlocking {

        with(mapError(errorJsonWithoutMessages)) {
            println(this)
            val error = this as SubError
            assertNotNull(error)
            assertEquals(404, error.code)
            assertEquals(SubError.DEFAULT_MESSAGE, error.message)
        }

        with(mapError(errorJsonWithMessages)) {
            val sub = this as SubError
            println(sub)
            assertNotNull(sub)
            assertEquals(403, sub.code)
            assertEquals("User Authentication error. Please try again.", sub.message)
            assertEquals("Authentication credentials missing.", sub.devMessage)
        }

        with(mapError("")) {
            println(this)
            val error = this as SubError
            assertNotNull(error)
            assertEquals(500, error.code)
            assertEquals(SubError.DEFAULT_MESSAGE, error.message)
        }

        with(mapError("{}")) {
            println(this)
            val error = this as SubError
            assertNotNull(error)
            assertEquals(500, error.code)
            assertEquals(SubError.DEFAULT_MESSAGE, error.message)
        }

        with(mapError("[{}]")) {
            println(this)
            val error = this as SubError
            assertNotNull(error)
            assertEquals(500, error.code)
            assertEquals(SubError.DEFAULT_MESSAGE, error.message)
        }
    }

    @Test
    fun test_deser_no_error() = runBlocking {

        with(mapErrorNullable(validJsonWithData)) {
            println(this)
            assertNull(this)
        }

        with(mapErrorNullable(validJsonWithoutData)) {
            println(this)
            assertNull(this)
        }

        with(mapErrorNullable("")) {
            println(this)
            assertNull(this)
        }

        with(mapErrorNullable("{}")) {
            println(this)
            assertNull(this)
        }

        with(mapErrorNullable("[{}]")) {
            println(this)
            assertNull(this)
        }
    }

    val mapper = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private suspend fun mapError(string: String?): APIError {
        // we cannot decode to the APIError instance because we will have to declare the polymorphic serializar
        // so instead, we decode it to the class implementing the interface and then return it as the interface
        return mapper.decodeNullableFrom<SubError>(string, "error") ?: SubError.default
    }

    private suspend fun mapErrorNullable(string: String?): APIError? {
        return mapper.decodeNullableFrom<SubError>(string, "error")
    }

    @Serializable
    data class SubError(
        @SerialName("status") val code: Int = 500,
        @SerialName("userMessage") override val message: String = DEFAULT_MESSAGE,
        @SerialName("devMessage") val devMessage: String? = null,
    ): APIError {

        companion object {
            val default: SubError get() = SubError()
            const val DEFAULT_MESSAGE = "This is the default message"
        }
    }
}