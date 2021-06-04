package com.egenesio.api_client.json_extension

import com.egenesio.api_client.json.elem
import com.egenesio.api_client.json.get
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonExtensionTest {
    @Test
    fun addition_isCorrect() {

        val baseElement = buildJsonObject {
            put("obj", buildJsonObject {
                put("a", "a")
            })
            put("array", buildJsonArray {
                add("string1")
                add("string2")
            })

            put("string", "this_is_the_string")
            put("bool", true)

            put("long", 2L)
            put("int", 2)
            put("float", 2f)
            put("double", 2.0.toDouble())
        }.elem


        val elem: JsonElement? = baseElement["obj"]
        val obj: JsonObject? = baseElement["obj"]
        val arr: JsonArray? = baseElement["array"]

        val string: String? = baseElement["string"]
        val bool: Boolean? = baseElement["bool"]

        val long: Long? = baseElement["long"]
        val int: Int? = baseElement["int"]
        val float: Float? = baseElement["float"]
        val double: Double? = baseElement["double"]


        println(elem)
        println(obj)
        println(arr)

        println(string)
        println(bool)

        println(long)
        println(int)
        println(float)
        println(double)

        assertEquals("this_is_the_string", string)
        assertEquals(true, bool)

    }
}