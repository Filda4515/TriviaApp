package com.example.triviaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun QuestionScreen(viewModel: QuestionViewModel, navController: NavController) {
    val score by viewModel.score.collectAsState()
    val questionState by viewModel.currentQuestion.collectAsState()
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            "Question #${score + 1}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(questionState.question, style = MaterialTheme.typography.titleLarge)

                val options =
                    questionState.incorrectAnswers.plus(questionState.correctAnswer).shuffled()
                options.forEach { option ->
                    Button(
                        onClick = {
                            val result = viewModel.onAnswerSelected(option)
                            if (!result) {
                                navController.navigate("gameover") {
                                    popUpTo("quiz") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Difficulty: ${settings.difficulty.label()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Type: ${settings.questionType.label()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}