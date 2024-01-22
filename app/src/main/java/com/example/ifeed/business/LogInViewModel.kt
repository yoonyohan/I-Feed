package com.example.ifeed.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val offlineRepository: Repository
): ViewModel() {

    private val _stateFlow = MutableStateFlow(LogInState())
    val state = _stateFlow.asStateFlow()


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
                    val userNameAndPasswordInDataBase = withContext(Dispatchers.IO) {
                        offlineRepository.getUserByName(_stateFlow.value.userName)
                    }
                    val userNameInDataBase = userNameAndPasswordInDataBase?.userName

                    if (userNameInDataBase == null) {
                        _stateFlow.update {
                            it.copy(
                                alert = "Account doesn't exist",
                                isLoggedIn = false
                            )
                        }
                    } else {
                        val passwordInDataBase = userNameAndPasswordInDataBase.password

                        if (passwordInDataBase == _stateFlow.value.password) {
                            _stateFlow.update {
                                it.copy(
                                    alert = "Welcome back!",
                                    isLoggedIn = true,
                                )
                            }

                            delay(500L)
                            _stateFlow.update {
                                it.copy(
                                    alert = "",
                                    userName = "",
                                    password = ""
                                )
                            }
                        } else {
                            _stateFlow.update {
                                it.copy(
                                    alert = "Password doesn't match",
                                    isLoggedIn = false
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    _stateFlow.update {
                        it.copy(
                            alert = "Error fetching user Data",
                            isLoggedIn = false
                        )
                    }
                    Log.e("LogInViewModelScope", "Fetching data from data base but it makes an error - $e")
                }
            }
        }
    }
}

