package com.example.moodjournal.repository

import com.example.moodjournal.entity.MoodEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MoodRepository: JpaRepository<MoodEntry, Long>
