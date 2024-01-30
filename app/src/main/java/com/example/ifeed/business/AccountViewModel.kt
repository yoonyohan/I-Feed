package com.example.ifeed.business

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ifeed.R
import com.example.ifeed.application.MyFeedApplication
import com.example.ifeed.data.AddImage
import com.example.ifeed.data.AppState
import com.example.ifeed.data.NetworkRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    private val fireNetWorkRepository: Lazy<NetworkRepository>,
    private val feedApplication: MyFeedApplication
) : ViewModel() {
    val state = appState

    init {
        viewModelScope.launch {
            if (fireNetWorkRepository.get().getProfileImageUrl().isNotEmpty()) {
                appState.update {
                    it.copy(
                        profileImageUrl = fireNetWorkRepository.get().getProfileImageUrl()
                    )
                }
            }
        }
    }

    suspend fun addProfileImage(uri: Uri) {
        try {

            when(val result = fireNetWorkRepository.get().addProfileImage(uri)) {
                is AddImage.ImageAddSuccess -> {
                    viewModelScope.launch {
                        fireNetWorkRepository.get().updateProfileImageRealNormal()
                    }
                }

                is AddImage.ImageAddError -> {
                    appState.update { // 2
                        it.copy(
                            alert = result.errorMsg,
                            updateProfileImageWithDataBases = false,
                            profileImageLoad = false
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AccountViewModel","Problem with upload image", e)
            appState.update {
                it.copy(
                    alert = "Something went wrong",
                    updateProfileImageWithDataBases = false,
                    profileImageLoad = false
                )
            }
        }
    }
}