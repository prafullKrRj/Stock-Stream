package com.prafullkumar.stockstream.data.remote.api

import android.util.Log
import okhttp3.ResponseBody.Companion.toResponseBody

class HttpLoggingInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val request = chain.request()

        Log.d("HTTP_REQUEST", "URL: ${request.url}")
        Log.d("HTTP_REQUEST", "Method: ${request.method}")
        Log.d("HTTP_REQUEST", "Headers: ${request.headers}")

        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()

        Log.d("HTTP_RESPONSE", "URL: ${response.request.url}")
        Log.d("HTTP_RESPONSE", "Status: ${response.code}")
        Log.d("HTTP_RESPONSE", "Time: ${endTime - startTime}ms")

        response.body?.let { responseBody ->
            val bodyString = responseBody.string()
            Log.d("HTTP_RESPONSE", "Body: $bodyString")

            return response.newBuilder()
                .body(bodyString.toResponseBody(responseBody.contentType()))
                .build()
        }

        return response
    }
}