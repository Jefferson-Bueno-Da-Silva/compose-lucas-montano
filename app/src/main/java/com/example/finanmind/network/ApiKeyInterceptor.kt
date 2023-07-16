package com.example.finanmind.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = originRequest.newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(newRequest)
    }
}