package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import com.example.ifeed.data.WritePost
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewPostViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    private val networkRepository: Lazy<NetworkRepository>
): ViewModel() {
    val state = appState

    fun resetAlert() {
        appState.update {
            it.copy(
                alert = ""
            )
        }
    }
    fun addContentToState(value: String) {
        appState.update {
            it.copy(
                content = value
            )
        }
    }

    suspend fun writePost() {
        when(val result = networkRepository.get().writeNewPost()) {
            is WritePost.PostCreationSuccess -> {
                appState.update {
                    it.copy(
                        alert = "Posted",
                        postCreationSuccess = true
                    )
                }
            }

            is WritePost.PostCreationError -> {
                appState.update {
                    it.copy(
                        alert = result.errorMsg,
                        postCreationSuccess = false
                    )
                }
            }
        }
    }
}