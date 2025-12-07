package com.example.triviaapp.data.mappers

import com.example.triviaapp.data.remote.CategoryApiDto
import com.example.triviaapp.domain.Category

fun CategoryApiDto.toDomain(): Category = Category(
    id = this.id,
    name = this.name
)