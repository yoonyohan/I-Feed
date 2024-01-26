package com.example.ifeed.data

import com.example.ifeed.business.FeedPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
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

data class UserMsg(
    val userName: String,
    val userId: String
) {
    constructor() : this("", "")
}

class FireBaseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val instance: FirebaseDatabase
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

    override fun readAllPosts(completion: (List<FeedPost>) -> Unit) {
        val postRef = instance.getReference("feed")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = mutableListOf<FeedPost>()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(FeedPost::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }

                completion(posts)
            }

            override fun onCancelled(error: DatabaseError) {
                completion(emptyList())
            }
        })
    }

    override fun getAllUsersForMsg(): kotlinx.coroutines.flow.Flow<List<UserMsg>> {
        TODO("Not yet implemented")
    }

}