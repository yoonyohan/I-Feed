package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
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

    fun addContentToState(value: String) {
        appState.update {
            it.copy(
                content = value
            )
        }
    }

    fun writePost() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.get().writeNewPost()
        }
    }
}