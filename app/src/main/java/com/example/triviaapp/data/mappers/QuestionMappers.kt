package com.example.triviaapp.data.mappers

import com.example.triviaapp.data.remote.QuestionApiDto
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionType

fun QuestionApiDto.toDomain(): Question {
    return Question(
        type = when (this.type) {
            "multiple" -> QuestionType.MULTIPLE
            "boolean" -> QuestionType.BOOLEAN
            else -> throw IllegalArgumentException("Unknown type: ${this.type}")
        },
        difficulty = when (this.difficulty) {
            "easy" -> Difficulty.EASY
            "medium" -> Difficulty.MEDIUM
            "hard" -> Difficulty.HARD
            else -> throw IllegalArgumentException("Unknown difficulty: ${this.difficulty}")
        },
        category = this.category,
        question = this.question,
        correctAnswer = this.correct_answer,
        incorrectAnswers = this.incorrect_answers
    )
}