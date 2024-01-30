package com.example.ifeed.business

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.R
import com.example.ifeed.application.MyFeedApplication
import com.example.ifeed.data.AppState
import com.example.ifeed.data.LogInStages
import com.example.ifeed.data.NetworkRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val networkRepository: Lazy<NetworkRepository>,
    firebaseAuth: FirebaseAuth,
    private val _appState: MutableStateFlow<AppState>,
    private val feedApplication: MyFeedApplication
): ViewModel() {
    val state = _appState

    init {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _appState.update {
                it.copy(
                    isLoggedIn = true
                )
            }
        }
    } // Check and confirm

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
            try {
                val result = networkRepository.get().logIn(
                    fieldsNotEmpty = _appState.value.userName.isEmpty() && _appState.value.password.isEmpty(),
                    userName = _appState.value.userName,
                    password = _appState.value.password
                )

                when(result) {
                    is LogInStages.LogInSuccess -> {
                        _appState.update {
                            it.copy(
                                alert = "Welcome back!",
                                isLoggedIn = true,
                            )
                        }
                    }
                    is LogInStages.LogInError -> {
                        _appState.update {
                            it.copy(
                                alert = result.errorMsg,
                                isLoggedIn = false,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LogInViewModel", "LogInFailed", e)
                _appState.update {
                    it.copy(
                        alert = feedApplication.getString(R.string.something_went_wrong),
                        isLoggedIn = false,
                    )
                }
            }
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

