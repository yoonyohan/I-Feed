package com.example.ifeed.business

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ifeed.data.Repository
import com.example.ifeed.data.UserMsg
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

data class MessageState(
    val chosenUser: State<Int?> = mutableStateOf(null),
    val usersForMsg: List<UserMsg> = emptyList()
)
class MessageViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseRepository: Repository
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(MessageState())
}