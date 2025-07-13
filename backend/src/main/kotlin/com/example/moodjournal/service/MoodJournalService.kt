package com.example.moodjournal.service

import com.example.moodjournal.dto.MoodEntryRequest
import com.example.moodjournal.dto.MoodEntryResponse
import com.example.moodjournal.dto.MoodSummaryResponse
import com.example.moodjournal.entity.MoodEntry
import com.example.moodjournal.repository.MoodRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MoodJournalService(
    private val moodRepository: MoodRepository
) {
    fun addEntry(request: MoodEntryRequest): MoodEntryResponse {
        val moodEntry = MoodEntry(
            0L,
            request.moodLevel,
            request.tags,
            request.comment
        )
        val savedMoodEntry = moodRepository.save(moodEntry)

        return MoodEntryResponse(
            savedMoodEntry.id,
            savedMoodEntry.moodLevel,
            savedMoodEntry.tags,
            savedMoodEntry.comment,
            savedMoodEntry.timestamp
        )
    }

    fun getAllMoodEntries(): List<MoodEntryResponse> {
        val entries = moodRepository.findAll()

        return entries.map { entry ->
            MoodEntryResponse(
                id = entry.id,
                moodLevel = entry.moodLevel,
                tags = entry.tags,
                comment = entry.comment,
                timestamp = entry.timestamp
            )
        }
    }

    fun getMoodEntryById(id: Long): MoodEntryResponse? {
        val foundEntry = moodRepository.findById(id)

        return foundEntry.map { entry ->
            MoodEntryResponse(
                id = entry.id,
                moodLevel = entry.moodLevel,
                tags = entry.tags,
                comment = entry.comment,
                timestamp = entry.timestamp
            )
        }.orElse(null)
    }

    fun getMoodSummary(): MoodSummaryResponse {
        val entries = moodRepository.findAll()

        val averageMoodLevel = entries.map { it.moodLevel }.average().toInt()
        val mostFrequentTags = entries.flatMap { it.tags }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }

        val entriesThisWeek = entries.count { it.timestamp.isAfter(LocalDateTime.now().minusDays(7)) }

        return MoodSummaryResponse(
            averageMoodLevel = averageMoodLevel,
            mostFrequentTags = mostFrequentTags,
            entriesThisWeek = entriesThisWeek
        )
    }
}
