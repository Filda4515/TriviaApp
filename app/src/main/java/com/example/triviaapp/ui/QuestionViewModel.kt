package com.example.triviaapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionRepository
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val difficulty = settingsRepository.getDifficulty()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Difficulty.ANY
        )

    private val _currentQuestion = MutableStateFlow(
        Question(
            type = QuestionType.MULTIPLE,
            difficulty = Difficulty.EASY,
            category = "Games",
            question = "Loading...",
            correctAnswer = "",
            incorrectAnswers = emptyList()
        )
    )
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _highScore = MutableStateFlow(0)
    val highScore = _highScore.asStateFlow()

    init {
        getNextQuestion()
    }

    private fun getOptions(q: Question): List<String> =
        (q.incorrectAnswers + q.correctAnswer).shuffled()

    fun getNextQuestion() {
        viewModelScope.launch {
            val currentDifficulty = difficulty.value
            try {
                val q = questionRepository.getQuestion(currentDifficulty)
                _currentQuestion.value = q
            } catch (t: Throwable) {
                Log.e("QuestionViewModel", "QuestionViewModel error: ${t.message}")
                throw t
            }
        }
    }

    fun onAnswerSelected(answer: String): Boolean {
        val correct = _currentQuestion.value.correctAnswer
        if (answer == correct) {
            _score.value += 1
            if (_score.value > _highScore.value) {
                _highScore.value = _score.value
            }
            Log.d("QuestionViewModel", "Correct answer: $answer, Score is now ${_score.value}")
            getNextQuestion()
            return true
        } else {
            Log.d("QuestionViewModel","Wrong answer: $answer (expected '$correct'). Game over with score ${_score.value}")
            return false
        }
    }

    fun resetGame() {
        _score.value = 0
        getNextQuestion()
    }
}