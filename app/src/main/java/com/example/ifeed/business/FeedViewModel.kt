package com.example.ifeed.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.PostFire
import com.example.ifeed.data.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class FeedState(
    val postFireList: List<PostFire> = emptyList(),
    val isLoading: Boolean = false,
    val alert: String? = null
)
class FeedViewModel(
    private val firebaseRepository: Repository,
    private val firebaseAuth: FirebaseAuth,
): ViewModel() {
    private val _stateFlow = MutableStateFlow(FeedState())

    init {
        viewModelScope.launch {
            try {
                firebaseRepository.getAllPosts().collect { posts ->
                    _stateFlow.value = _stateFlow.value.copy(postFireList = posts, isLoading = false)
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        alert = "Error fetching posts: ${e.message}"
                    )
                }
            } finally {
                _stateFlow.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }

        }
    }

    val state = _stateFlow.asStateFlow()
}