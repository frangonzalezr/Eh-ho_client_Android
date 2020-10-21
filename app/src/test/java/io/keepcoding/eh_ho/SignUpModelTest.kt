package io.keepcoding.eh_ho

import io.keepcoding.eh_ho.data.SignUpModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpModelTest {

    @Test
    fun toJson_isCorrect() {
        val model = SignUpModel(
            "test",
            "test@test.com",
            "12345678"
        )

        val json = model.toJson()

        assertEquals("test", json.get("name"))
        assertEquals("test", json.get("username"))
        assertEquals("test@test.com", json.get("email"))
        assertEquals("12345678", json.get("password"))
        assertEquals(true, json.get("active"))
        assertEquals(true, json.get("approved"))
    }
}