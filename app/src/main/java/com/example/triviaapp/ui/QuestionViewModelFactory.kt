package com.example.triviaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.triviaapp.domain.HighscoreRepository
import com.example.triviaapp.domain.QuestionRepository
import com.example.triviaapp.domain.SettingsRepository

class QuestionViewModelFactory(
    private val questionRepository: QuestionRepository,
    private val settingsRepository: SettingsRepository,
    private val highscoreRepository: HighscoreRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(
                questionRepository,
                settingsRepository,
                highscoreRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}