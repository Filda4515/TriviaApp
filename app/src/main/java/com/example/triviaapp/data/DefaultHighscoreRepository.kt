package com.example.triviaapp.data

import com.example.triviaapp.data.local.HighscoreDao
import com.example.triviaapp.data.local.HighscoreEntity
import com.example.triviaapp.domain.HighscoreRepository
import com.example.triviaapp.domain.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultHighscoreRepository(
    private val dao: HighscoreDao
) : HighscoreRepository {
    override fun getHighscore(settings: Settings): Flow<Int?> {
        return dao.getHighscoreEntityFlow(
            settings.category.id,
            settings.difficulty.name,
            settings.questionType.name
        ).map { it?.score }
    }

    override suspend fun saveHighscore(settings: Settings, score: Int) {
        val entity = HighscoreEntity(
            categoryId = settings.category.id,
            difficulty = settings.difficulty.name,
            questionType = settings.questionType.name,
            score = score
        )
        dao.upsert(entity)
    }

    override suspend fun resetHighscores() {
        dao.deleteAll()
    }
}