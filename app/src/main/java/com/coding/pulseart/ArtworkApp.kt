package com.coding.pulseart

import android.app.Application
import com.coding.pulseart.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ArtworkApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ArtworkApp)
            androidLogger()
            modules(appModule)
        }
    }
}