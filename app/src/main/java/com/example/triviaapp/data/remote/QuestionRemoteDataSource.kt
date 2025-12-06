package com.example.triviaapp.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.serialization.json.Json

class QuestionRemoteDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getQuestion(difficulty: String?): QuestionApiDto {
        return withContext(Dispatchers.IO) {
            try {
                val url = StringBuilder("https://opentdb.com/api.php?")
                url.append("amount=1")
                url.append("&category=15")
                if (difficulty != null)
                    url.append("&difficulty=$difficulty")
                url.append("&type=multiple")

                val response = URL(url.toString()).readText()
                val apiResponse = json.decodeFromString<QuestionApiResponse>(response)
                when (apiResponse.response_code) {
                    0 -> apiResponse.results.first()
                    1 -> throw IllegalStateException("No Results")
                    2 -> throw IllegalArgumentException("Invalid Parameter")
                    3 -> throw IllegalStateException("Token Not Found")
                    4 -> throw IllegalStateException("Token Empty")
                    5 -> throw IllegalStateException("Rate Limit")
                    else -> throw IllegalStateException("Unknown API response code: ${apiResponse.response_code}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error: $e")
                throw e
            }
        }
    }
}