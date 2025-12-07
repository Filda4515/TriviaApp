package com.example.triviaapp.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "highscores",
    indices = [Index(value = ["categoryId", "difficulty", "questionType"], unique = true)]
)
data class HighscoreEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,
    val difficulty: String,     // store enum.name()
    val questionType: String,   // store enum.name()
    val score: Int
)