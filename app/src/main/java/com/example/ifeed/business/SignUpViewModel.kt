package com.example.ifeed.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    private val fireNetWorkRepository: Lazy<NetworkRepository>
): ViewModel() {
    val state = appState

    init {
        viewModelScope.launch {
            appState.collect { appState ->
                if(appState.toAccountCreation) {
                    fireNetWorkRepository.get().updateAccountDataNormal()
                    fireNetWorkRepository.get().updateAccountDataLive()
                }
            }
        }
    }

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

    fun onEmail(value: Boolean) {
        appState.update {
            it.copy(
                emailAddressOn = value
            )
        }
    }

    fun onPhoneNumberOn(value: Boolean) {
        appState.update {
            it.copy(
                phoneNumberOn = value
            )
        }
    }

    fun addEmailOrPhoneNumberToState(value: String, type: String) {
        if (type == "email") {
            appState.update {
                it.copy(
                    email = value
                )
            }
        }

        if (type == "phone") {
            appState.update {
                it.copy(
                    phoneNumber = value
                )
            }
        }
    }

    fun addPasswordToState(value: String) {
        appState.update {
            it.copy(
                password = value
            )
        }
    }

    fun createAnAccount() {
        viewModelScope.launch {
            fireNetWorkRepository.get().accountCreation()
        }
    }

    fun upDateAccountViaDataBases() {
        viewModelScope.launch {

        }
    }
}
