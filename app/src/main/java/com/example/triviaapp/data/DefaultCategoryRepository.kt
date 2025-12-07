package com.example.triviaapp.data

import com.example.triviaapp.data.mappers.toDomain
import com.example.triviaapp.data.remote.CategoryRemoteDataSource
import com.example.triviaapp.domain.Category
import com.example.triviaapp.domain.CategoryRepository

class DefaultCategoryRepository(
    private val categoryRemoteDataSource: CategoryRemoteDataSource = CategoryRemoteDataSource()
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        val dtoList = categoryRemoteDataSource.getCategories()
        return dtoList.map { it.toDomain() }
    }
}