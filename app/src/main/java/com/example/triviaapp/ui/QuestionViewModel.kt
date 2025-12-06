package com.example.triviaapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.Question
import com.example.triviaapp.domain.QuestionRepository
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<Settings> = settingsRepository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings.DEFAULT
        )

    private val _currentQuestion = MutableStateFlow(
        Question(
            type = QuestionType.MULTIPLE,
            difficulty = Difficulty.EASY,
            category = "",
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

    private val questionQueue = ArrayDeque<Question>()
    private var isFetching = false

    fun getOptions(q: Question): List<String> =
        if (q.type == QuestionType.BOOLEAN)
            (q.incorrectAnswers + q.correctAnswer).sortedDescending()
        else
            (q.incorrectAnswers + q.correctAnswer).shuffled()

    fun getNextQuestion(passedSettings: Settings? = null) {
        viewModelScope.launch {
            if (passedSettings != null) {
                questionQueue.clear()
                getQuestionsIfNeeded(5, passedSettings)
            } else if (questionQueue.size <= 1) {
                getQuestionsIfNeeded(5)
            }

            getNextQuestionFromQueue()
        }
    }

    private fun getNextQuestionFromQueue() {
        if (questionQueue.isNotEmpty()) {
            _currentQuestion.value = questionQueue.removeFirst()
        } else {
            Log.e("QuestionViewModel", "Empty queue")
        }
    }

    private suspend fun getQuestionsIfNeeded(amount: Int, passedSettings: Settings? = null) {
        if (isFetching) return
        if (questionQueue.isNotEmpty()) return
        isFetching = true
        try {
            val currentSettings = passedSettings ?: settings.value
            val list = questionRepository.getQuestions(currentSettings, amount)
            questionQueue.addAll(list)
        } catch (e: Exception) {
            Log.e("QuestionViewModel", "Error: ${e.message}")
        } finally {
            isFetching = false
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
            Log.d(
                "QuestionViewModel",
                "Wrong answer: $answer (expected '$correct'). Game over with score ${_score.value}"
            )
            return false
        }
    }

    fun resetGame() {
        _score.value = 0
        getNextQuestion()
    }
}