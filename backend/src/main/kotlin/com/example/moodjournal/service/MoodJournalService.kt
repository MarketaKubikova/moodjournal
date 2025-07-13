package com.example.moodjournal.service

import com.example.moodjournal.dto.MoodEntryRequest
import com.example.moodjournal.dto.MoodEntryResponse
import com.example.moodjournal.entity.MoodEntry
import com.example.moodjournal.repository.MoodRepository
import org.springframework.stereotype.Service

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
}
