package com.example.triviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triviaapp.data.DefaultCategoryRepository
import com.example.triviaapp.data.DefaultQuestionRepository
import com.example.triviaapp.data.DefaultSettingsRepository
import com.example.triviaapp.ui.GameOverScreen
import com.example.triviaapp.ui.QuestionScreen
import com.example.triviaapp.ui.QuestionViewModel
import com.example.triviaapp.ui.QuestionViewModelFactory
import com.example.triviaapp.ui.SettingsScreen
import com.example.triviaapp.ui.SettingsViewModel
import com.example.triviaapp.ui.SettingsViewModelFactory
import com.example.triviaapp.ui.theme.TriviaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaAppTheme {
                val questionRepository = DefaultQuestionRepository()
                val settingsRepository = DefaultSettingsRepository(this)
                val categoryRepository = DefaultCategoryRepository()

                val questionViewModel: QuestionViewModel = viewModel(
                    factory = QuestionViewModelFactory(questionRepository, settingsRepository)
                )

                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModelFactory(settingsRepository, categoryRepository)
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(questionViewModel, settingsViewModel)
                }
            }
        }
    }
}

@Composable
private fun Navigation(questionViewModel: QuestionViewModel, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "settings") {
        composable("settings") {
            SettingsScreen(
                questionViewModel = questionViewModel,
                settingsViewModel = settingsViewModel,
                navController = navController
            )
        }
        composable("quiz") {
            QuestionScreen(viewModel = questionViewModel, navController = navController)
        }
        composable("gameover") {
            GameOverScreen(viewModel = questionViewModel, navController = navController)
        }
    }
}
