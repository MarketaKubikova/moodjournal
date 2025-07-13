package com.example.moodjournal.dto

import java.time.LocalDateTime

data class MoodEntryResponse(
    val id: Long,
    val moodLevel: Int,
    val tags: List<String> = emptyList(),
    val comment: String? = null,
    val timestamp: LocalDateTime
)
