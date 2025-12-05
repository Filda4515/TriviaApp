package com.example.triviaapp.domain

data class Question(
    val type: QuestionType,
    val difficulty: Difficulty,
    val category: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)
