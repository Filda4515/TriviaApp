package com.example.triviaapp.domain

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}