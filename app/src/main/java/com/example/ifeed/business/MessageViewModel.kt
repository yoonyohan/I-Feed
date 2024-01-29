package com.example.ifeed.business

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ifeed.data.NetworkRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class MessageState(
    val chosenUser: State<Int?> = mutableStateOf(null),
//    val usersForMsg: List<UserMsg> = emptyList()
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val networkRepository: Lazy<NetworkRepository>
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(MessageState())
}