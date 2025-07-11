package com.example.moodjournal.service

import com.example.moodjournal.model.MoodEntry
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MoodJournalServiceTest {
    private val service = MoodJournalService()

    @Test
    fun addEntry() {
        val entry = MoodEntry(
            id = 0L,
            moodLevel = 5,
            tags = listOf("happy"),
            comment = "Feeling great"
        )

        val savedEntry = service.addEntry(entry)

        assertNotNull(savedEntry.id)
        assertEquals(5, savedEntry.moodLevel)
        assertEquals(listOf("happy"), savedEntry.tags)
        assertEquals("Feeling great", savedEntry.comment)
    }

    @Test
    fun getAllMoodEntries() {
        val entry1 = MoodEntry(
            id = 0L,
            moodLevel = 3,
            tags = listOf("neutral"),
            comment = "Just okay"
        )
        val entry2 = MoodEntry(
            id = 1L,
            moodLevel = 7,
            tags = listOf("excited"),
            comment = "Feeling pumped"
        )

        service.addEntry(entry1)
        service.addEntry(entry2)

        val allEntries = service.getAllMoodEntries()

        assertEquals(2, allEntries.size)
        assertTrue(allEntries.any { it.moodLevel == 3 && it.comment == "Just okay" })
        assertTrue(allEntries.any { it.moodLevel == 7 && it.comment == "Feeling pumped" })
    }

    @Test
    fun getMoodEntryById_moodEntryFound_shouldReturnMoodEntry() {
        val entry = MoodEntry(
            id = 0L,
            moodLevel = 4,
            tags = listOf("content"),
            comment = "Feeling content"
        )

        val savedEntry = service.addEntry(entry)
        val foundEntry = service.getMoodEntryById(savedEntry.id)

        assertNotNull(foundEntry)
        assertEquals(1L, foundEntry?.id)
        assertEquals(4, foundEntry?.moodLevel)
        assertEquals("Feeling content", foundEntry?.comment)
    }

    @Test
    fun getMoodEntryById_moodEntryNotFound_shouldReturnNull() {
        val foundEntry = service.getMoodEntryById(999L)
        assertNull(foundEntry)
    }
}
