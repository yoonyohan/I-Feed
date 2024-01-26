package com.example.ifeed.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class FeedApplication: Application() {
    lateinit var feedAppContainer: AppContainer
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()
        feedAppContainer = FeedAppContainer(
            FirebaseAuth.getInstance(),
            FirebaseFirestore.getInstance(),
            FirebaseDatabase.getInstance("https://i-feed-post-default-rtdb.asia-southeast1.firebasedatabase.app/")
        )
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance("https://i-feed-post-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }
}