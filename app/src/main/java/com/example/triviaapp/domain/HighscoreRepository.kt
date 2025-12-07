package com.example.triviaapp.domain

import kotlinx.coroutines.flow.Flow

interface HighscoreRepository {
    fun getHighscore(settings: Settings): Flow<Int?>
    suspend fun saveHighscore(settings: Settings, score: Int)
    suspend fun resetHighscores()
}