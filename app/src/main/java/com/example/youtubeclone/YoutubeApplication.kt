package com.example.youtubeclone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class YoutubeApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}