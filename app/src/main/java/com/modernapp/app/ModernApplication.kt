package com.modernapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModernApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
