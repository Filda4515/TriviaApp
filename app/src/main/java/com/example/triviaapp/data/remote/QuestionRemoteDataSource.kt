package com.example.triviaapp.data.remote

import android.util.Log
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection

class QuestionRemoteDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getQuestion(settings: Settings): QuestionApiDto {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val url = StringBuilder("https://opentdb.com/api.php?")
                url.append("amount=1")
                url.append("&category=15")
                if (settings.difficulty != Difficulty.ANY)
                    url.append("&difficulty=${settings.difficulty.apiParam()}")
                if (settings.questionType != QuestionType.ANY)
                    url.append("&type=${settings.questionType.apiParam()}")
                url.append("&encode=url3986")

                connection = URL(url.toString()).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                val response = if (connection.responseCode >= 400) {
                    connection.errorStream.bufferedReader().use { it.readText() }
                } else {
                    connection.inputStream.bufferedReader().use { it.readText() }
                }

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
            } finally {
                connection?.disconnect()
            }
        }
    }
}