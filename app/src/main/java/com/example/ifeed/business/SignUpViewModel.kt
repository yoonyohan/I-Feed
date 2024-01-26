package com.example.ifeed.business

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpState(
    val firstName: String = "",
    val lastName: String= "",
    val middleName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val emailAddressOn: Boolean = true,
    val phoneNumberOn: Boolean = false,
    val alert: String = "",
    val isLoading: Boolean = false,
    val userName: String = "",
    val accountCreationSuccess: Boolean = false,
    val toLoggedIn: Boolean = false,
)

class SignUpViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _stateFlow = MutableStateFlow(SignUpState())
    val state = _stateFlow.asStateFlow()

    fun resetNames() {
        _stateFlow.update {
            it.copy(
                firstName = "",
                lastName = "",
                phoneNumber = "",
                email = "",
                password = ""
            )
        }
    }
    fun resetContact() {
        _stateFlow.update {
            it.copy(
                phoneNumber = "",
                email = "",
                password = ""
            )
        }
    }
    fun resetPassword() {
        _stateFlow.update {
            it.copy(
                password = ""
            )
        }
    }

    fun addFirstNameToState(value: String) {
        if (value.length <= 11) {
            _stateFlow.update {
                it.copy(
                    firstName = value
                )
            }
        }
   }
    fun addLastNameToState(value: String) {
        if (value.length <= 11) {
            _stateFlow.update {
                it.copy(
                    lastName = value
                )
            }
        }
    }
    fun onEmail(value: Boolean) {
        _stateFlow.update {
            it.copy(
                emailAddressOn = value
            )
        }
    }
    fun onPhoneNumberOn(value: Boolean) {
        _stateFlow.update {
            it.copy(
                phoneNumberOn = value
            )
        }
    }
    fun addEmailOrPhoneNumberToState(value: String, type: String) {
        if (type == "email") {
            _stateFlow.update {
                it.copy(
                    email = value
                )
            }
        }

        if (type == "phone") {
            _stateFlow.update {
                it.copy(
                    phoneNumber = value
                )
            }
        }
    }
    fun addPasswordToState(value: String) {
        _stateFlow.update {
            it.copy(
                password = value
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun accountCreation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_stateFlow.value.emailAddressOn) {
                    firebaseAuth.createUserWithEmailAndPassword(_stateFlow.value.email, _stateFlow.value.password)
                        .addOnCompleteListener {
                            task ->
                            if (task.isComplete) {
                                _stateFlow.update {
                                    it.copy(
                                        email = "",
                                        password = "",
                                        isLoading = true,
                                        accountCreationSuccess = false
                                    )
                                }

                            } else {
                                _stateFlow.update {
                                    it.copy(
                                        alert = "Problem with creating an account",
                                        isLoading = true
                                    )
                                }
                            }
                        }.addOnFailureListener { exception ->
                            val errorMessage: String = when(exception) {
                                is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one."
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Email"
                                is FirebaseAuthUserCollisionException -> "Account already exist for this email address"
                                else -> "Problem with creating an account"
                            }
                            _stateFlow.update {
                                it.copy(
                                    alert = errorMessage,
                                    isLoading = true
                                )
                            }
                        }
                }

                if (_stateFlow.value.phoneNumberOn) {
                    firebaseAuth.createUserWithEmailAndPassword(_stateFlow.value.phoneNumber, _stateFlow.value.password)
                        .addOnCompleteListener {
                            task ->
                            if (task.isSuccessful) {
                                _stateFlow.update {
                                    it.copy(
                                        phoneNumber = "",
                                        password = "",
                                        isLoading = true
                                    )
                                }

                                userDataUpdate()
                            } else {
                                _stateFlow.update {
                                    it.copy(
                                        alert = "Problem with creating an account",
                                        isLoading = true
                                    )
                                }
                            }
                        }.addOnFailureListener {
                            exception ->
                            val errorMessage: String = when(exception) {
                                is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Phone number"
                                is FirebaseAuthUserCollisionException -> "Account already exist for this phone number"
                                else -> "Problem with creation an account"
                            }
                            _stateFlow.update {
                                it.copy(
                                    alert = errorMessage,
                                    isLoading = true
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                // Handle other exceptions if needed
                _stateFlow.update {
                    it.copy(
                        alert = "Something went wrong",
                        isLoading = true,
                        toLoggedIn = false,
                        accountCreationSuccess = false
                    )
                }
            }
        }
    }

    private fun userDataUpdate() {
        val currentUser = firebaseAuth.currentUser
        Log.d("FireStore", "User Id: ${currentUser?.uid}")
        if (currentUser != null) {
            val userData = hashMapOf(
                "userId" to currentUser.uid,
                "firstName" to _stateFlow.value.firstName,
                "lastName" to _stateFlow.value.lastName,
                "middleName" to _stateFlow.value.middleName,
                "fullName" to "${_stateFlow.value.firstName} ${_stateFlow.value.lastName}",
                "userName" to _stateFlow.value.userName
            )

            firestore.collection("userData").document(currentUser.uid)
                .set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }.addOnFailureListener {
                    e ->
                    Log.w(TAG, "Error writing document", e)
                }
        }
    }
}