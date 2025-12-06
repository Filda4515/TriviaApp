package com.example.triviaapp.domain

interface QuestionRepository {
    suspend fun getQuestion(difficulty: Difficulty): Question
}