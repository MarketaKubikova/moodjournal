package com.example.moodjournal.controller

import com.example.moodjournal.model.MoodEntry
import com.example.moodjournal.service.MoodJournalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/mood"])
class MoodJournalController(
    private val service: MoodJournalService
) {

    @PostMapping
    fun addMood(@RequestBody entry: MoodEntry): ResponseEntity<MoodEntry> {
        val saved = service.addEntry(entry)
        return ResponseEntity.ok(saved)
    }

    @GetMapping
    fun getAll(): List<MoodEntry> = service.getAllMoodEntries()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<MoodEntry> =
        service.getMoodEntryById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
}
