package com.example.triviaapp.data

import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.ui.QuestionRepository

class DefaultQuestionRepository: QuestionRepository {
    override suspend fun getQuestion(): Question {
        return Question(
            type = QuestionType.MULTIPLE,
            difficulty = Difficulty.EASY,
            category = "Games",
            question = "Who made Silksong?",
            correctAnswer = "Team Cherry",
            incorrectAnswers = listOf("Xbox Game Studios", "Riot", "Warhorse Studios")
        )
    }
}