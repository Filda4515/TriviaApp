package com.example.triviaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository): ViewModel() {

    val settings: StateFlow<Settings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings.DEFAULT
        )

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            settingsRepository.setDifficulty(difficulty)
        }
    }

    fun setType(type: QuestionType) {
        viewModelScope.launch {
            settingsRepository.setType(type)
        }
    }
}