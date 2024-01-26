package com.example.ifeed.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AddNewPostState(
    val content: String = "",
    val userID: Int? = null,
    val alert: String? = null,
    val postCreationSuccess: Boolean = false
)

data class FeedPost(
    val name: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0
)

class AddNewPostViewModel(
    private val offlineRepository: Repository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firestore: FirebaseFirestore,
): ViewModel() {
    private val _stateflow = MutableStateFlow(AddNewPostState())
    val state = _stateflow.asStateFlow()

    fun addContentToState(value: String) {
        _stateflow.update {
            it.copy(
                content = value
            )
        }
    }

    fun writePost() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                val postRef = firebaseDatabase.getReference("feed")
                val userId = currentUser.uid

                val postModel = FeedPost(
                    name = firestore.collection("userData").document(userId).get().await().getString("fullName") ?: "",
                    userId = userId,
                    content = _stateflow.value.content,
                    timestamp = System.currentTimeMillis()
                )

                val newPostRef = postRef.push()
                newPostRef.setValue(postModel)
                    .addOnCompleteListener {
                        task ->
                        if (task.isSuccessful) {
                            _stateflow.update {
                                it.copy(
                                    alert = "Posted",
                                    postCreationSuccess = true
                                )
                            }
                        }
                    }.addOnFailureListener {
                            exception ->
                        val errorMessage = when (exception) {
                            is FirebaseFirestoreException -> "Firebase realtime exception: ${exception.message ?: "Something went wrong"}"
                            else -> "Failed to create post: ${exception.message ?: "Something went wrong"}"
                        }

                        _stateflow.update {
                            it.copy(
                                alert = errorMessage,
                                postCreationSuccess = false
                            )
                        }

                        Log.e("AddNewPost","Firestore exception", exception)
                    }
            }
        }
    }
}