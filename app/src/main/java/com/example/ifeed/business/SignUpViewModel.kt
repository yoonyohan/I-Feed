package com.example.ifeed.business

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.R
import com.example.ifeed.application.MyFeedApplication
import com.example.ifeed.data.AccountCreationStages
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
class SignUpViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    private val fireNetWorkRepository: Lazy<NetworkRepository>,
    private val feedApplication: MyFeedApplication
): ViewModel() {
    val state = appState

    fun resetNames() {
        appState.update {
            it.copy(
                firstName = "",
                lastName = "",
                phoneNumber = "",
                email = "",
                password = ""
            )
        }
    }

    fun resetAlert() {
        appState.update {
            it.copy(
                alert = ""
            )
        }
    }

    fun resetContact() {
        appState.update {
            it.copy(
                phoneNumber = "",
                email = "",
                password = ""
            )
        }
    }

    fun resetPassword() {
        appState.update {
            it.copy(
                password = ""
            )
        }
    }

    fun addFirstNameToState(value: String) {
        if (value.length <= 11) {
            appState.update {
                it.copy(
                    firstName = value
                )
            }
        }
    }

    fun addLastNameToState(value: String) {
        if (value.length <= 11) {
            appState.update {
                it.copy(
                    lastName = value
                )
            }
        }
    }

    fun addEmailOrPhoneNumberToState(value: String) {
        appState.update {
            it.copy(
                email = value
            )
        }
    }

    fun addPasswordToState(value: String) {
        appState.update {
            it.copy(
                password = value
            )
        }
    }

    suspend fun createAnAccount() {
        try {
            val result = fireNetWorkRepository.get().accountCreation(
                email = appState.value.email,
                password = appState.value.password
            )

            when(result) {
                is AccountCreationStages.AccountCreationError -> {
                    appState.update {
                        it.copy(
                            alert = result.errorMessage,
                            isLoading = false,
                            isLoggedIn = true,
                            accountCreationSuccess = false
                        )
                    }
                }
                is AccountCreationStages.AccountCreationSuccess -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (appState.value.toLoggedIn) {
                            fireNetWorkRepository.get().updateAccountDataNormal()
                            fireNetWorkRepository.get().updateAccountDataLive()
                        }
                    }
                    appState.update {
                        it.copy(
                            accountCreationSuccess = true,
                            isLoading = false,
                            toLoggedIn = true,
                            alert = feedApplication.getString(R.string.successfully_created_an_account)
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SignUpViewModel", "Problem with account creation", e)
            appState.update {
                it.copy(
                    alert = feedApplication.getString(R.string.problem_with_creating_an_account),
                    isLoading = true,
                    toLoggedIn = false,
                    accountCreationSuccess = false
                )
            }
        }
    }
}
