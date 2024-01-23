package com.example.ifeed.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow
import kotlin.coroutines.cancellation.CancellationException

data class PostFire(
    val title: String,
    val description: String,
    val topic: String,
    val authorId: String,
    val userEmail: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "","" ,0)
}

class FireBaseRepository(
    private val firebaseAuth: FirebaseAuth
): Repository {
    override fun getAllPosts() = callbackFlow {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("posts")

        val listenerRegistration = collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    cancel(CancellationException("Error fetching posts", exception))
                    return@addSnapshotListener
                }

                val posts = snapshot?.toObjects(PostFire::class.java) ?: emptyList()
                trySend(posts)
            }

        awaitClose { listenerRegistration.remove() }
    }

}