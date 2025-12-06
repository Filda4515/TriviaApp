package com.example.triviaapp.data

import android.util.Log
import com.example.triviaapp.data.mappers.toDomain
import com.example.triviaapp.data.remote.QuestionRemoteDataSource
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.QuestionRepository
import com.example.triviaapp.domain.Settings

class DefaultQuestionRepository(
    private val questionRemoteDataSource: QuestionRemoteDataSource = QuestionRemoteDataSource()
): QuestionRepository {

    val fallbackQuestion: Question = Question(
        type = QuestionType.MULTIPLE,
        difficulty = Difficulty.EASY,
        category = "Entertainment: Video Games",
        question = "Who made Silksong?",
        correctAnswer = "Team Cherry",
        incorrectAnswers = listOf("Xbox Game Studios", "Riot", "Warhorse Studios")
    )

    override suspend fun getQuestions(settings: Settings, amount: Int): List<Question> {
        val dtoList = questionRemoteDataSource.getQuestions(settings, amount)
        val domain = dtoList.mapNotNull { dto ->
            try {
                dto.toDomain()
            } catch (e: Exception) {
                Log.e("DefaultQuestionRepository", "Error: ${e.message}")
                null
            }
        }
        return domain.ifEmpty { listOf(fallbackQuestion) }
    }
}