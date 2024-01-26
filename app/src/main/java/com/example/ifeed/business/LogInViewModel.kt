package com.example.ifeed.business

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LogInState(
    val userName: String = "",
    val password: String = "",
    val alert: String? = null,
    val isEmpty: Boolean = false,
    val isLoggedIn: Boolean = false
)

class LogInViewModel(
    private val offlineRepository: Repository,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _stateFlow = MutableStateFlow(LogInState())
    val state = _stateFlow.asStateFlow()

    init {
        _stateFlow.update {
            it.copy(
                isLoggedIn = firebaseAuth.currentUser != null
            )
        }
    }


    fun userNameToState(value: String) {
        _stateFlow.update {
            it.copy(
                userName = value
            )
        }
    }
    fun passwordToState(value: String) {
        _stateFlow.update {
            it.copy(
                password = value
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun logIn() {
        viewModelScope.launch {
            if (_stateFlow.value.userName.isEmpty() && _stateFlow.value.password.isEmpty()) {
                _stateFlow.update {
                    it.copy(
                        isEmpty = true,
                        alert = "Input Fields cannot be empty"
                    )
                }
            } else {
                try {
                    withContext(Dispatchers.IO) {
                        firebaseAuth.signInWithEmailAndPassword(
                            _stateFlow.value.userName,
                            _stateFlow.value.password
                        ).addOnCompleteListener {
                                task ->
                            if (task.isSuccessful) {
                                _stateFlow.update {
                                    it.copy(
                                        alert = "Welcome back!",
                                        isLoggedIn = true,
                                    )
                                }
                            }
                        }.addOnFailureListener { exception ->
                            // Handle specific login exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidUserException -> "User not found"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                else -> "Authentication failed: ${exception.message ?: "Something went wrong"}"
                            }

                            _stateFlow.update {
                                it.copy(
                                    alert = errorMessage,
                                    isLoggedIn = false,
                                )
                            }
                        }
                    }

                } catch (e: Exception) {
                    // Handle other exceptions if needed
                    _stateFlow.update {
                        it.copy(
                            alert = "Something went wrong",
                            isLoggedIn = false,
                        )
                    }
                }
            }
        }
    }

    fun resetState() {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(
                    userName = "",
                    password = "",
                    alert = "",
                    isEmpty = false,
                    isLoggedIn = false
                )
            }
        }
    }
}

