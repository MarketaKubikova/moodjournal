package com.example.moodjournal.dto

data class MoodEntryRequest(
    val moodLevel: Int,
    val tags: List<String> = emptyList(),
    val comment: String? = null
)
