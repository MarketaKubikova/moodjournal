package com.example.moodjournal.dto

data class MoodSummaryResponse(
    val averageMoodLevel: Int,
    val mostFrequentTags: List<String> = emptyList(),
    val entriesThisWeek: Int
)
