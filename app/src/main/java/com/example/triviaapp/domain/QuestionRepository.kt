package com.example.triviaapp.domain

interface QuestionRepository {
    suspend fun getQuestions(settings: Settings, amount: Int): List<Question>
}