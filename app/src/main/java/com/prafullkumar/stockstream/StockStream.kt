package com.prafullkumar.stockstream

import android.app.Application
import com.prafullkumar.stockstream.di.homeModule
import com.prafullkumar.stockstream.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class StockStream : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StockStream)
            modules(networkModule, homeModule)
        }
    }
}