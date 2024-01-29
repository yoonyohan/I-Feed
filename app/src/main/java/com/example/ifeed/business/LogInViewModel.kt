package com.example.ifeed.business

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val networkRepository: Lazy<NetworkRepository>,
    private val _appState: MutableStateFlow<AppState>
): ViewModel() {
    val state = _appState

    fun userNameToState(value: String) {
        _appState.update {
            it.copy(
                userName = value
            )
        }
    }
    fun passwordToState(value: String) {
        _appState.update {
            it.copy(
                password = value
            )
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    fun logIn() {
        viewModelScope.launch {
            networkRepository.get().logIn()
        }
    }
    fun resetState() {
        _appState.update {
            it.copy(
                userName = "",
                password = "",
                alert = "",
                isEmpty = false,
                isLoggedIn = false
            )
        }
    }

    fun resetAlert() {
        _appState.update {
            it.copy(
                alert = ""
            )
        }
    }
}

