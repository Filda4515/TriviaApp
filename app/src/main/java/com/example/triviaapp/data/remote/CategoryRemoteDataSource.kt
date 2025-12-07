package com.example.triviaapp.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

class CategoryRemoteDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getCategories(): List<CategoryApiDto> = withContext(Dispatchers.IO) {
        val url = "https://opentdb.com/api_category.php"
        try {
            val response = URL(url).readText()
            val apiResponse = json.decodeFromString<CategoryApiResponse>(response)
            apiResponse.triviaCategories
        } catch (e: Exception) {
            Log.e("API", "Failed to fetch categories: ${e.message}")
            emptyList()
        }
    }
}