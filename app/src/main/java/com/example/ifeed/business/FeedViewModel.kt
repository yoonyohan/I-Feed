package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val networkRepository: Lazy<NetworkRepository>,
    private val _appState: MutableStateFlow<AppState>
): ViewModel() {
    val state = _appState
    init {
        readAllPosts()
    }
    fun readAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.get().readAllPosts {
                    list ->
                _appState.update {
                    it.copy(
                        postFireList = list
                    )
                }
            }
        }
    }
}