package com.example.triviaapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String
) {
    companion object {
        val DEFAULT = Category(id = -1, name = "Any Category")
    }
}