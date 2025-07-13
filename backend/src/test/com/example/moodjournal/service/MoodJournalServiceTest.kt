package com.example.moodjournal.service

import com.example.moodjournal.dto.MoodEntryRequest
import com.example.moodjournal.entity.MoodEntry
import com.example.moodjournal.repository.MoodRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class MoodJournalServiceTest(
    @Mock
    private val repository: MoodRepository
) {
    private val service = MoodJournalService(repository)

    @Test
    fun addEntry() {
        val entry = MoodEntry(
            id = 1L,
            moodLevel = 5,
            tags = listOf("happy"),
            comment = "Feeling great"
        )

        `when`(repository.save(any())).thenReturn(entry)

        val savedEntry = service.addEntry(MoodEntryRequest(
            moodLevel = 5,
            tags = listOf("happy"),
            comment = "Feeling great"
        ))

        assertEquals(1L, savedEntry.id)
        assertEquals(5, savedEntry.moodLevel)
        assertEquals(listOf("happy"), savedEntry.tags)
        assertEquals("Feeling great", savedEntry.comment)
        assertNotNull(savedEntry.timestamp)
        verify(repository, times(1)).save(any(MoodEntry::class.java))
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

        `when`(repository.findAll()).thenReturn(listOf(entry1, entry2))

        val allEntries = service.getAllMoodEntries()

        assertEquals(2, allEntries.size)
        assertTrue(allEntries.any { it.moodLevel == 3 && it.comment == "Just okay" })
        assertTrue(allEntries.any { it.moodLevel == 7 && it.comment == "Feeling pumped" })
        verify(repository, times(1)).findAll()
    }

    @Test
    fun getMoodEntryById_moodEntryFound_shouldReturnMoodEntry() {
        val entry = MoodEntry(
            id = 1L,
            moodLevel = 4,
            tags = listOf("content"),
            comment = "Feeling content"
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(entry))

        val foundEntry = service.getMoodEntryById(1L)

        assertEquals(1L, foundEntry?.id)
        assertEquals(4, foundEntry?.moodLevel)
        assertEquals("Feeling content", foundEntry?.comment)
        verify(repository, times(1)).findById(1L)
    }

    @Test
    fun getMoodEntryById_moodEntryNotFound_shouldReturnNull() {
        `when`(repository.findById(999L)).thenReturn(Optional.empty())

        val foundEntry = service.getMoodEntryById(999L)

        assertNull(foundEntry)
        verify(repository, times(1)).findById(999L)
    }

    @Test
    fun getMoodSummary() {
        val moodEntry = MoodEntry(
            id = 1L,
            moodLevel = 5,
            tags = listOf("happy", "productive"),
            comment = "Great day",
            timestamp = LocalDateTime.now().minusDays(8)
        )
        val moodEntry2 = MoodEntry(
            id = 2L,
            moodLevel = 3,
            tags = listOf("neutral"),
            comment = "Just okay",
            timestamp = LocalDateTime.now().minusDays(3)
        )
        val moodEntry3 = MoodEntry(
            id = 3L,
            moodLevel = 7,
            tags = listOf("excited", "productive"),
            comment = "Feeling pumped",
            timestamp = LocalDateTime.now().minusDays(7)
        )
        val moodEntry4 = MoodEntry(
            id = 4L,
            moodLevel = 2,
            tags = listOf("sad"),
            comment = "Not a good day",
            timestamp = LocalDateTime.now().minusDays(1)
        )
        val moodEntry5 = MoodEntry(
            id = 5L,
            moodLevel = 6,
            tags = listOf("happy", "relaxed"),
            comment = "Feeling good",
            timestamp = LocalDateTime.now()
        )

        `when`(repository.findAll()).thenReturn(listOf(moodEntry, moodEntry2, moodEntry3, moodEntry4, moodEntry5))

        val summary = service.getMoodSummary()

        assertEquals(4, summary.averageMoodLevel)
        assertTrue(summary.mostFrequentTags.contains("happy").and(summary.mostFrequentTags.contains("productive")))
        assertEquals(summary.entriesThisWeek, 3)
    }
}
