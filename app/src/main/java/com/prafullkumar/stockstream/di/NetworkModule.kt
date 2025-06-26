package com.prafullkumar.stockstream.di

import com.prafullkumar.stockstream.data.remote.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single<ApiService> {
        Retrofit.Builder().baseUrl("https://www.alphavantage.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(
                ApiService::class.java
            )
    }

}