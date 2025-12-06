package com.example.triviaapp.domain

enum class QuestionType : HasLabel {
    ANY, MULTIPLE, BOOLEAN;

    override fun label(): String = when (this) {
        ANY -> "Any Type"
        MULTIPLE -> "Multiple Choice"
        BOOLEAN -> "True / False"
    }

    fun apiParam(): String? = when (this) {
        ANY -> null
        MULTIPLE -> "multiple"
        BOOLEAN -> "boolean"
    }
}