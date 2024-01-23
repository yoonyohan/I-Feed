package com.example.ifeed.application

import com.example.ifeed.data.FireBaseRepository
import com.example.ifeed.data.Repository
import com.google.firebase.auth.FirebaseAuth

class FeedAppContainer(
    private val firebaseAuth: FirebaseAuth
): AppContainer {
    override val firebaseRepository: Repository by lazy {
        FireBaseRepository(firebaseAuth)
    }
}