package com.example.finanmind.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {

    private const val API_KEY = "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"
    private const val BASE_URL = "https://api.openai.com"

    fun provideAiService(): AiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(provideApiKeyInterceptor())) // Http Client + Interceptor
            .addConverterFactory(GsonConverterFactory.create()) // converte JSON para Objeto
            .build()
            .create(AiService::class.java) // Interface da Api
    }

    private fun provideApiKeyInterceptor(): ApiKeyInterceptor {
        return ApiKeyInterceptor(API_KEY)
    }

    private fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logInterceptor)
            .build()
    }
}