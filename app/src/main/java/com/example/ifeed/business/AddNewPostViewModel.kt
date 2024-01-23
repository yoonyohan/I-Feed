package com.example.ifeed.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Timestamp

data class AddNewPostState(
    val title: String = "",
    val description: String = "",
    val topic: String = "",
    val userID: Int? = null,
    val alert: String? = null,
    val postCreationSuccess: Boolean = false
)

class AddNewPostViewModel(
    private val offlineRepository: Repository,
    private val firebaseAuth: FirebaseAuth,
): ViewModel() {
    private val _stateflow = MutableStateFlow(AddNewPostState())
    val state = _stateflow.asStateFlow()

    fun addTitleToState(value: String) {
        _stateflow.update {
            it.copy(
                title = value
            )
        }
    }

    fun addDescriptionToState(value: String) {
        _stateflow.update {
            it.copy(
                description = value
            )
        }
    }

    fun addTopicToState(value: String) {
        _stateflow.update {
            it.copy(
                topic = value
            )
        }
    }

    suspend fun addNewPost() {
        if (_stateflow.value.title.isNotEmpty() && _stateflow.value.description.isNotEmpty() && _stateflow.value.topic.isNotEmpty()) {

            viewModelScope.launch {
                try {
                    val currentUser = firebaseAuth.currentUser

                    if (currentUser != null) {
                        val userEmail = currentUser.email

                        val postData = hashMapOf(
                            "title" to _stateflow.value.title,
                            "description" to _stateflow.value.description,
                            "topic" to _stateflow.value.topic,
                            "authorId" to currentUser.uid,
                            "userEmail" to userEmail,
                            "timestamp" to System.currentTimeMillis()
                        )
                        val db = Firebase.firestore

                        db.collection("posts").add(postData)
                            .addOnSuccessListener {
                                _stateflow.update {
                                    it.copy(
                                        alert = "Post created",
                                        postCreationSuccess = true
                                    )
                                }
                            }
                            .addOnFailureListener {
                                exception ->
                                val errorMessage = when (exception) {
                                    is FirebaseFirestoreException -> "Firestore exception: ${exception.message ?: "Something went wrong"}"
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

                    } else {
                        _stateflow.update {
                            it.copy(
                                alert = "Something went wrong",
                                postCreationSuccess = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    _stateflow.update {
                        it.copy(
                            postCreationSuccess = false,
                            alert = "Something went wrong"
                        )
                    }
                }
            }

        } else {
            _stateflow.update {
                it.copy(
                    alert = "Input fields cannot be empty",
                    postCreationSuccess = false
                )
            }
        }
    }
}