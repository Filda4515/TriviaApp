package com.example.triviaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, navController: NavController) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)

        SettingsSection(
            title = "Select Difficulty:",
            options = Difficulty.entries,
            selected = settings.difficulty,
            onSelectionChanged = { newDifficulty ->
                viewModel.setDifficulty(newDifficulty)
            }
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        SettingsSection(
            title = "Select Type:",
            options = QuestionType.entries,
            selected = settings.questionType,
            onSelectionChanged = { newType ->
                viewModel.setType(newType)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("quiz") {
                    popUpTo("settings") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }
    }
}

@Composable
fun <T> SettingsSection(
    title: String,
    options: List<T>,
    selected: T,
    onSelectionChanged: (T) -> Unit
) where T : Enum<T>, T : com.example.triviaapp.domain.HasLabel {
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
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = (-8).dp
            ) {
                options.forEach { option ->
                    FilterChip(
                        selected = selected == option,
                        onClick = { onSelectionChanged(option) },
                        label = {
                            Text(
                                text = option.label(),
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}