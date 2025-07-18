package com.example.moodjournal.controller

import com.example.moodjournal.dto.MoodEntryRequest
import com.example.moodjournal.dto.MoodEntryResponse
import com.example.moodjournal.dto.MoodSummaryResponse
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
    fun addMood(@RequestBody entry: MoodEntryRequest): ResponseEntity<MoodEntryResponse> {
        val saved = service.addEntry(entry)
        return ResponseEntity.ok(saved)
    }

    @GetMapping
    fun getAll(): List<MoodEntryResponse> = service.getAllMoodEntries()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<MoodEntryResponse> =
        service.getMoodEntryById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/summary")
    fun getSummary(): ResponseEntity<MoodSummaryResponse> {
        val summary = service.getMoodSummary()
        return ResponseEntity.ok(summary)
    }
}
