package com.example.triviaapp.ui

import com.example.triviaapp.domain.Question

interface QuestionRepository {
    suspend fun getQuestion(): Question
}