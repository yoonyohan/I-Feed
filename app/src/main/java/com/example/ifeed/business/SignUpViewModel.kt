package com.example.ifeed.business

import android.app.RecoverableSecurityException
import android.database.StaleDataException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.data.Repository
import com.example.ifeed.data.User
import com.example.ifeed.data.UserNameAndPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException

data class SignUpState(
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val alert: String? = "",
    val toLoggedIn: Boolean = false,
    val accountCreationSuccess: Boolean = false
)
class SignUpViewModel(
    private val offlineRepository: Repository,
    private val firebaseAuth: FirebaseAuth
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

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun accountCreation() {
        if (_stateFlow.value.userName.isNotEmpty() && _stateFlow.value.password.isNotEmpty() && _stateFlow.value.confirmPassword.isNotEmpty()) {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val passwordEquality: Boolean = _stateFlow.value.password == _stateFlow.value.confirmPassword
                    if (passwordEquality) {
                        firebaseAuth.createUserWithEmailAndPassword(
                            _stateFlow.value.userName,
                            _stateFlow.value.confirmPassword
                        ).addOnCompleteListener {
                            task ->
                            if (task.isSuccessful) {
                                _stateFlow.update {
                                    it.copy(
                                        userName = "",
                                        password = "",
                                        confirmPassword = "",
                                        toLoggedIn = true,
                                        accountCreationSuccess = true
                                    )
                                }
                            }
                        } .addOnFailureListener { exception ->
                            // Handle specific sign-up exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one."
                                is FirebaseAuthInvalidCredentialsException -> "Invalid email address"
                                is FirebaseAuthUserCollisionException -> "Account already exists for this email address"
                                else -> "Sign-up failed: ${exception.message ?: "Something went wrong"}"
                            }

                            _stateFlow.update {
                                it.copy(
                                    userName = "",
                                    password = "",
                                    confirmPassword = "",
                                    alert = errorMessage,
                                    toLoggedIn = false,
                                    accountCreationSuccess = false
                                )
                            }
                        }
                    } else {
                        _stateFlow.update {
                            it.copy(
                                userName = "",
                                password = "",
                                confirmPassword = "",
                                alert = "Password doesn't match",
                                toLoggedIn = false,
                                accountCreationSuccess = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Handle other exceptions if needed
                    _stateFlow.update {
                        it.copy(
                            userName = "",
                            password = "",
                            confirmPassword = "",
                            alert = "Something went wrong",
                            toLoggedIn = false,
                            accountCreationSuccess = false
                        )
                    }
                }
            }
        } else {
            _stateFlow.update {
                it.copy(
                    alert = "Input fields cannot be empty",
                    accountCreationSuccess = false,
                    toLoggedIn = false
                )
            }
        }
    }
}