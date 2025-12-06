package com.example.triviaapp.domain

enum class Difficulty {
    ANY, EASY, MEDIUM, HARD;

    fun label(): String = when (this) {
        ANY -> "Any Difficulty"
        EASY -> "Easy"
        MEDIUM -> "Medium"
        HARD -> "Hard"
    }

    fun apiParam(): String? = when (this) {
        ANY -> null
        EASY -> "easy"
        MEDIUM -> "medium"
        HARD -> "hard"
    }
}