package com.example.moodjournal.model

import java.time.LocalDateTime

data class MoodEntry(
    val id: Long,
    val moodLevel: Int,
    val tags: List<String> = emptyList(),
    val comment: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
