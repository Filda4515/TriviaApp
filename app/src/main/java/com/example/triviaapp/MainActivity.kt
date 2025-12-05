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
import com.example.triviaapp.data.DefaultQuestionRepository
import com.example.triviaapp.ui.GameOverScreen
import com.example.triviaapp.ui.QuestionScreen
import com.example.triviaapp.ui.QuestionViewModel
import com.example.triviaapp.ui.QuestionViewModelFactory
import com.example.triviaapp.ui.theme.TriviaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TriviaAppTheme {
                val repository = DefaultQuestionRepository()
                val viewModel: QuestionViewModel = viewModel(
                    factory = QuestionViewModelFactory(repository)
                )

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(viewModel)
                }
            }
        }
    }
}

@Composable
private fun Navigation(viewModel: QuestionViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "quiz") {
        composable("quiz") { QuestionScreen(viewModel = viewModel, navController = navController) }
        composable("gameover") { GameOverScreen(viewModel = viewModel, navController = navController) }
    }
}
