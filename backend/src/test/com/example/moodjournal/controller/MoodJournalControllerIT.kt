package com.example.moodjournal.controller

import com.example.moodjournal.dto.MoodEntryRequest
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
open class MoodJournalControllerIT @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @Test
    fun `should add mood entry`() {
        val request = MoodEntryRequest(
            moodLevel = 9,
            tags = listOf("excited"),
            comment = "Big news today"
        )

        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.moodLevel", `is`(9)))
            .andExpect(jsonPath("$.tags", hasSize<Any>(1)))
            .andExpect(jsonPath("$.comment", `is`("Big news today")))
    }

    @Test
    fun `should retrieve all mood entries`() {
        val entryRequest1 = MoodEntryRequest(
            moodLevel = 3,
            tags = listOf("neutral"),
            comment = "Just okay"
        )
        val entryRequest2 = MoodEntryRequest(
            moodLevel = 7,
            tags = listOf("excited"),
            comment = "Feeling pumped"
        )

        mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest1))
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest2))
        )
            .andExpect(status().isOk)

        mockMvc.perform(get("/api/mood"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].moodLevel", `is`(3)))
            .andExpect(jsonPath("$[1].moodLevel", `is`(7)))
    }

    @Test
    fun `should retrieve mood entry by ID`() {
        val request = MoodEntryRequest(
            moodLevel = 5,
            tags = listOf("happy"),
            comment = "Feeling good"
        )

        val savedEntry = mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andReturn()
            .response.contentAsString

        val savedEntryId = objectMapper.readTree(savedEntry).get("id").asLong()

        mockMvc.perform(get("/api/mood/$savedEntryId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(savedEntryId.toInt())))
            .andExpect(jsonPath("$.moodLevel", `is`(5)))
            .andExpect(jsonPath("$.tags", hasSize<Any>(1)))
            .andExpect(jsonPath("$.comment", `is`("Feeling good")))
    }

    @Test
    fun `should return 404 for non-existent mood entry`() {
        mockMvc.perform(get("/api/mood/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return mood summary`() {
        val entryRequest1 = MoodEntryRequest(
            moodLevel = 5,
            tags = listOf("happy"),
            comment = "Feeling great"
        )
        val entryRequest2 = MoodEntryRequest(
            moodLevel = 3,
            tags = listOf("neutral"),
            comment = "Just okay"
        )

        mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest1))
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/api/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest2))
        )
            .andExpect(status().isOk)

        mockMvc.perform(get("/api/mood/summary"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.averageMoodLevel", `is`(4)))
            .andExpect(jsonPath("$.mostFrequentTags", hasSize<Any>(2)))
            .andExpect(jsonPath("$.entriesThisWeek", `is`(2)))
    }
}
