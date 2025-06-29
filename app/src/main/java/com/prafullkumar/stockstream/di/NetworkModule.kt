package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.remote.api.ApiService
import com.prafullkumar.stockstream.data.remote.api.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<ApiService> {
        Retrofit.Builder().baseUrl("https://www.alphavantage.co/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(
                ApiService::class.java
            )
    }

}