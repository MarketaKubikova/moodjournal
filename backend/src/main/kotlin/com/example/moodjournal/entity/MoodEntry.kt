package com.example.moodjournal.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class MoodEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "mood_level")
    val moodLevel: Int,
    @ElementCollection(fetch = FetchType.EAGER)
    val tags: List<String> = emptyList(),
    val comment: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
