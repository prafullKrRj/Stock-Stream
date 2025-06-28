package com.prafullkumar.stockstream

import android.app.Application
import com.prafullkumar.stockstream.di.homeModule
import com.prafullkumar.stockstream.di.networkModule
import com.prafullkumar.stockstream.di.newsModule
import com.prafullkumar.stockstream.di.searchModule
import com.prafullkumar.stockstream.di.settingsModule
import com.prafullkumar.stockstream.di.watchListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class StockStream : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StockStream)
            modules(
                networkModule,
                watchListModule,
                homeModule,
                searchModule,
                newsModule,
                settingsModule
            )
        }
    }
}