package com.example.triviaapp.data.mappers

import com.example.triviaapp.data.remote.QuestionApiDto
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionType
import java.net.URLDecoder

fun QuestionApiDto.toDomain(): Question = Question(
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
    question = URLDecoder.decode(this.question, "UTF-8"),
    correctAnswer = URLDecoder.decode(this.correct_answer, "UTF-8"),
    incorrectAnswers = incorrect_answers.map { URLDecoder.decode(it, "UTF-8") }
)