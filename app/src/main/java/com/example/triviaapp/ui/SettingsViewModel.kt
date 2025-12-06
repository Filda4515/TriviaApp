package com.example.triviaapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository): ViewModel() {

    val difficulty: StateFlow<Difficulty> = settingsRepository.getDifficulty()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Difficulty.ANY
        )

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            settingsRepository.setDifficulty(difficulty)
        }
    }
}