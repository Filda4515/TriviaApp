package com.example.triviaapp.data

import com.example.triviaapp.data.mappers.toDomain
import com.example.triviaapp.data.remote.QuestionRemoteDataSource
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.ui.QuestionRepository


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

    override suspend fun getQuestion(): Question {
        return try {
            val dto = questionRemoteDataSource.getQuestion()
            return dto.toDomain()
        } catch (e: Exception) {
            fallbackQuestion
        }
    }
}