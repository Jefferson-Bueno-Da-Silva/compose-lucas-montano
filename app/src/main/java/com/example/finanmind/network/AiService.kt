package com.example.finanmind.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AiService {

    @POST("v1/completions")
    suspend fun completions(@Body prompt: OpenAIPrompt): Response<OpenAIResponse>
}