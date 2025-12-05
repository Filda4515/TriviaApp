package com.example.triviaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GameOverScreen(viewModel: QuestionViewModel, navController: NavController) {
    val score by viewModel.score.collectAsState()
    val highScore by viewModel.highScore.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Over!", style = MaterialTheme.typography.titleLarge)
        Text("Score: $score", style = MaterialTheme.typography.titleMedium)
        Text("Highscore: $highScore", style = MaterialTheme.typography.titleMedium)

        Button(onClick = {
            viewModel.resetGame()
            navController.navigate("quiz") {
                popUpTo("quiz") { inclusive = true }
            }
        }) {
            Text("Play again")
        }
    }
}