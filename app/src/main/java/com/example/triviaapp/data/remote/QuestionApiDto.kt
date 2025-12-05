package com.example.triviaapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class QuestionApiDto(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)

@Serializable
data class QuestionApiResponse(
    val response_code: Int,
    val results: List<QuestionApiDto>
)