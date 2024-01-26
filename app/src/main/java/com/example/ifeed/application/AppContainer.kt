package com.example.ifeed.application

import com.example.ifeed.data.Repository
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val firebaseRepository: Repository
}