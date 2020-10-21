package io.keepcoding.eh_ho

import io.keepcoding.eh_ho.data.CreateTopicModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateTopicModelTest {

    @Test
    fun toJson_isCorrect() {
        val model = CreateTopicModel("Title", "Content")
        val json = model.toJson()

        assertEquals("Title", json.get("title"))
        assertEquals("Content", json.get("raw"))
    }
}