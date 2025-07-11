package com.example.moodjournal.service

import com.example.moodjournal.model.MoodEntry
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class MoodJournalService {
    private val entries = mutableListOf<MoodEntry>()
    private val idCounter = AtomicLong(1)

    fun addEntry(entry: MoodEntry): MoodEntry {
        val withId = entry.copy(id = idCounter.getAndIncrement())
        entries.add(withId)

        return withId
    }

    fun getAllMoodEntries(): List<MoodEntry> = entries.toList()

    fun getMoodEntryById(id: Long): MoodEntry? = entries.find { it.id == id }
}
