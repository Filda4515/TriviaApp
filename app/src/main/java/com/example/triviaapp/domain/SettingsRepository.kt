package com.example.triviaapp.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDifficulty(): Flow<Difficulty>
    suspend fun setDifficulty(difficulty: Difficulty)
}