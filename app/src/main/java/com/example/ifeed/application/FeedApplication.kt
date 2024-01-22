package com.example.ifeed.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class FeedApplication: Application() {
    lateinit var feedAppContainer: AppContainer
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        feedAppContainer = FeedAppContainer(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }
}