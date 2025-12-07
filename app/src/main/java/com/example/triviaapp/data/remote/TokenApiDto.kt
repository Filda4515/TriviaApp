package com.example.triviaapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenApiDto(
    @SerialName("response_code") val responseCode: Int,
    @SerialName("response_message") val responseMessage: String? = null,
    val token: String? = null
)