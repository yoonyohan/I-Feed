package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import com.example.ifeed.data.Repository
import com.example.ifeed.data.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignUpState(
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val alert: String? = "",
    val accountCreationSuccess: Boolean = false
)
class SignUpViewModel(
    private val offlineRepository: Repository
): ViewModel() {
    private val _stateFlow = MutableStateFlow(SignUpState())
    val state = _stateFlow.asStateFlow()

    fun addUserNameToState(value: String) {
        _stateFlow.update {
            it.copy(
                userName = value
            )
        }
    }

    fun addPasswordToState(value: String) {
        _stateFlow.update {
            it.copy(
                password = value
            )
        }
    }

    fun addConfirmPasswordToState(value: String) {
        _stateFlow.update {
            it.copy(
                confirmPassword = value
            )
        }
    }

    suspend fun accountCreation() {
        if (_stateFlow.value.userName.isNotEmpty() && _stateFlow.value.password.isNotEmpty() && _stateFlow.value.confirmPassword.isNotEmpty()) {
            val userData = offlineRepository.getUserByName(_stateFlow.value.userName)
            val userNameFromDataBase = userData?.userName

            if (userNameFromDataBase != null) {
                _stateFlow.update {
                    it.copy(
                        alert = "Account already exist",
                        accountCreationSuccess = false
                    )
                }
            } else {
                if (_stateFlow.value.password == _stateFlow.value.confirmPassword) {
                    val newUser = User(
                        userName = _stateFlow.value.userName,
                        password = _stateFlow.value.confirmPassword
                    )
                    offlineRepository.addNewUser(newUser)
//                    delay(1000)
//                    _stateFlow.update {
//                        it.copy(
//                            accountCreationSuccess = true,
//                            userName = "",
//                            password = "",
//                            confirmPassword = ""
//                        )
//                    }
                } else {
                    _stateFlow.update {
                        it.copy(
                            alert = "Password doesn't match"
                        )
                    }
                }
            }
        } else {
            _stateFlow.update {
                it.copy(
                    alert = "Input fields cannot be empty"
                )
            }
        }
    }
}