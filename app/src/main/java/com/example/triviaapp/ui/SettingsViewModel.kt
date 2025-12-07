package com.example.triviaapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.Category
import com.example.triviaapp.domain.CategoryRepository
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val settings: StateFlow<Settings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings.DEFAULT
        )

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            try {
                val list = categoryRepository.getCategories()
                _categories.value = list
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error: ${e.message}")
                throw e
            }
        }
    }

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

    fun setCategory(category: Category) {
        viewModelScope.launch {
            settingsRepository.setCategory(category)
        }
    }
}