package com.example.triviaapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryApiDto(
    val id: Int,
    val name: String
)

@Serializable
data class CategoryApiResponse(
    @SerialName("trivia_categories")
    val triviaCategories: List<CategoryApiDto> = emptyList()
)