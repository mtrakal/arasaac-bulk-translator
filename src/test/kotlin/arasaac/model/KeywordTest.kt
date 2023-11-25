package arasaac.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class KeywordTest {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        encodeDefaults = true
        explicitNulls = false
    }

    @Test
    fun testSerialize() {
        val keyword = Keyword(
            hasLocution = true,
            keyword = "test",
            meaning = "test meaning",
            plural = "tests",
            type = 1,
        )
        val expectedJson = """{"hasLocution":true,"keyword":"test","meaning":"test meaning","plural":"tests","type":1}"""
        val actualJson = json.encodeToString(keyword)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testSerialize2() {
        val keyword = Keyword(
            keyword = "test",
            meaning = "test meaning",
            plural = "tests",
            type = 1,
        )
        val expectedJson = """{"hasLocution":false,"keyword":"test","meaning":"test meaning","plural":"tests","type":1}"""
        val actualJson = json.encodeToString(keyword)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun `test serialize meaning null`() {
        val keyword = Keyword(
            keyword = "test",
            meaning = null,
            plural = "tests",
            type = 1,
        )
        val expectedJson = """{"hasLocution":false,"keyword":"test","plural":"tests","type":1}"""
        val actualJson = json.encodeToString(keyword)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun `test serialize meaning not filled`() {
        val keyword = Keyword(
            keyword = "test",
            plural = "tests",
            type = 1,
        )
        val expectedJson = """{"hasLocution":false,"keyword":"test","plural":"tests","type":1}"""
        val actualJson = json.encodeToString(keyword)
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testDeserialize() {
        val json = """{"hasLocution":true,"keyword":"test","meaning":"test meaning","plural":"tests","type":1}"""
        val expectedKeyword = Keyword(
            hasLocution = true,
            keyword = "test",
            meaning = "test meaning",
            plural = "tests",
            type = 1,
        )
        val actualKeyword = Json.decodeFromString<Keyword>(json)
        assertEquals(expectedKeyword, actualKeyword)
    }
}
