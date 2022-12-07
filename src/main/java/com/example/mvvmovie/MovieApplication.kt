package com.example.mvvmovie

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.mvvmovie.utils.SessionManager


class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.initializeContext(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}