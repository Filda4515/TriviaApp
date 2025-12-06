package com.example.triviaapp.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    suspend fun setDifficulty(difficulty: Difficulty)
    suspend fun setType(type: QuestionType)
}