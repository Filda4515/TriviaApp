package com.example.triviaapp.domain

data class Settings(
    val difficulty: Difficulty = Difficulty.ANY,
    val questionType: QuestionType = QuestionType.ANY,
    val category: Category = Category.DEFAULT
){
    companion object {
        val DEFAULT = Settings()
    }
}
