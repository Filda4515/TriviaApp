package com.example.triviaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.triviaapp.domain.Category
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    questionViewModel: QuestionViewModel,
    navController: NavController
) {
    val settings by settingsViewModel.settings.collectAsState()
    val categories by settingsViewModel.categories.collectAsState()
    val highscore by questionViewModel.currentHighscore.collectAsState()

    var showConfirmReset by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsSection(
                title = "Select Difficulty:",
                options = Difficulty.entries,
                selected = settings.difficulty,
                onSelectionChanged = { newDifficulty ->
                    settingsViewModel.setDifficulty(newDifficulty)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            SettingsSection(
                title = "Select Type:",
                options = QuestionType.entries,
                selected = settings.questionType,
                onSelectionChanged = { newType ->
                    settingsViewModel.setType(newType)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
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
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select Category:",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (categories.isEmpty()) {
                        Text("Loading categories...", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        val allCategories = listOf(Category.DEFAULT) + categories

                        CategoryDropdown(
                            categories = allCategories,
                            selectedCategory = settings.category,
                            onCategorySelected = { category ->
                                settingsViewModel.setCategory(category)
                            }
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                questionViewModel.getNextQuestion(settings)
                navController.navigate("quiz") {
                    popUpTo("settings") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .width(IntrinsicSize.Max),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Highscore: ${highscore ?: 0}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = { showConfirmReset = true },
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text("Reset Highscores")
            }

        }
    }

    if (showConfirmReset) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showConfirmReset = false },
            title = { Text("Reset highscores?") },
            text = { Text("This will delete ALL saved highscores for every settings. Are you sure?") },
            confirmButton = {
                Button(onClick = {
                    questionViewModel.resetHighscores()
                    showConfirmReset = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmReset = false }) {
                    Text("Cancel")
                }
            }
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = selectedCategory.name,
                onValueChange = {},
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                val groupedCategories = categories.groupBy {
                    if (it.name.contains(":")) it.name.substringBefore(":") else "Other"
                }

                groupedCategories.forEach { (groupName, groupItems) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = groupName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        onClick = {}
                    )

                    groupItems.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onCategorySelected(category)
                                expanded = false
                            },
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}