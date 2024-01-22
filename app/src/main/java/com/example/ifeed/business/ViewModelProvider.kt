package com.example.ifeed.business

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ifeed.application.FeedApplication

object Provider {
    val factory = viewModelFactory {
        initializer {
            LogInViewModel(feedApplication().feedAppContainer.offlineRepository)
        }

        initializer {
            SignUpViewModel(feedApplication().feedAppContainer.offlineRepository)
        }
    }
}


fun CreationExtras.feedApplication(): FeedApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FeedApplication)