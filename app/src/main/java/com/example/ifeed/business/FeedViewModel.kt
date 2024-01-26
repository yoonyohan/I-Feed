package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.PostFire
import com.example.ifeed.data.Repository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedState(
    val postFireList: List<FeedPost> = emptyList(),
    val isLoading: Boolean = false,
    val alert: String? = null
)
class FeedViewModel(
    private val firebaseRepository: Repository,
    private val firebaseAuth: FirebaseAuth,
): ViewModel() {
    private val _stateFlow = MutableStateFlow(FeedState())

    init {
        readAllPosts()
    }

    fun readAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.readAllPosts {
                list ->
                _stateFlow.update {
                    it.copy(
                        postFireList = list
                    )
                }
            }
        }
    }

    val state = _stateFlow.asStateFlow()
}