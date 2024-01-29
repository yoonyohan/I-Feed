package com.example.ifeed.business

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    private val fireNetWorkRepository: Lazy<NetworkRepository>
) : ViewModel() {
    val state = appState.asStateFlow()
    fun addProfileImage(uri: Uri) {
        viewModelScope.launch {
            fireNetWorkRepository.get().addProfileImage(uri)
            delay(500L)
            if (appState.value.updateProfileImageWithDataBases) {
                fireNetWorkRepository.get().updateProfileImageRealNormal()
            }
        }
    }
}