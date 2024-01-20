package com.example.ifeed.application

import android.app.Application

class FeedApplication: Application() {
    lateinit var feedAppContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        feedAppContainer = FeedAppContainer(this)
    }
}