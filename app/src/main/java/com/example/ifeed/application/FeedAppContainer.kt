package com.example.ifeed.application

import com.example.ifeed.data.FireBaseRepository
import com.example.ifeed.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class FeedAppContainer(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val instance: FirebaseDatabase
): AppContainer {
    override val firebaseRepository: Repository by lazy {
        FireBaseRepository(firebaseAuth, firestore, instance)
    }
}