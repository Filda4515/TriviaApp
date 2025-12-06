package com.example.triviaapp.domain

enum class Difficulty : HasLabel {
    ANY, EASY, MEDIUM, HARD;

    override fun label(): String = when (this) {
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