package com.example.triviaapp.domain

interface QuestionRepository {
    suspend fun getQuestion(settings: Settings): Question
}