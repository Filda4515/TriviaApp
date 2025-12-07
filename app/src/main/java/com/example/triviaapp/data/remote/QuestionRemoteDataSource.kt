package com.example.triviaapp.data.remote

import android.util.Log
import com.example.triviaapp.domain.Category
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.URL
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection

class QuestionRemoteDataSource {
    private val json = Json { ignoreUnknownKeys = true }
    private var token: String? = null
    private val tokenMutex = Mutex()

    private suspend fun ensureToken(): String = tokenMutex.withLock {
        token?.let { return it }

        val url = "https://opentdb.com/api_token.php?command=request"
        return try {
            val response = withContext(Dispatchers.IO) {
                URL(url).readText()
            }
            val dto = json.decodeFromString<TokenApiDto>(response)
            val newToken = dto.token ?: throw IllegalStateException("No token in response")
            token = newToken
            Log.d("API", "Got new token: $newToken")
            newToken
        } catch (e: Exception) {
            Log.e("API", "Failed to request token: ${e.message}")
            throw e
        }
    }

    private suspend fun resetToken(currentToken: String) {
        val resetUrl = "https://opentdb.com/api_token.php?command=reset&token=$currentToken"
        try {
            val resp = withContext(Dispatchers.IO) {
                URL(resetUrl).readText()
            }
            val dto = json.decodeFromString<TokenApiDto>(resp)
            Log.d("API", "Reset token response: ${dto.responseCode} ${dto.responseMessage}")
        } catch (e: Exception) {
            Log.e("API", "Failed to reset token: ${e.message}")
            throw e
        }
    }

    suspend fun getQuestions(settings: Settings, amount: Int): List<QuestionApiDto> =
        withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            val maxAttempts = 2

            for (attempt in 1..maxAttempts) {
                try {
                    val currentToken = ensureToken()

                    val url = StringBuilder("https://opentdb.com/api.php?")
                    url.append("amount=$amount")
                    if (settings.category != Category.DEFAULT)
                        url.append("&category=${settings.category.id}")
                    if (settings.difficulty != Difficulty.ANY)
                        url.append("&difficulty=${settings.difficulty.apiParam()}")
                    if (settings.questionType != QuestionType.ANY)
                        url.append("&type=${settings.questionType.apiParam()}")
                    url.append("&encode=url3986")
                    url.append("&token=$currentToken")

                    Log.d("API", "Calling API:$url")

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
                        0 -> return@withContext apiResponse.results
                        1 -> throw IllegalStateException("No Results")
                        2 -> throw IllegalArgumentException("Invalid Parameter")
                        3, 4 -> {
                            Log.w(
                                "API",
                                "Token problem (code=${apiResponse.response_code}). Resetting token and retrying."
                            )
                            try {
                                resetToken(currentToken)
                            } catch (resetEx: Exception) {
                                Log.e("API", "Failed to reset token: ${resetEx.message}")
                            }
                            tokenMutex.withLock { token = null }

                            continue
                        }

                        5 -> throw IllegalStateException("Rate Limit")
                        else -> throw IllegalStateException("Unknown API response code: ${apiResponse.response_code}")
                    }
                } catch (e: Exception) {
                    Log.e("API", "Error: $e")
                    if (attempt == maxAttempts) throw e
                } finally {
                    connection?.disconnect()
                    connection = null
                }
            }

            throw IllegalStateException("Failed to get questions after $maxAttempts attempts")
        }
}