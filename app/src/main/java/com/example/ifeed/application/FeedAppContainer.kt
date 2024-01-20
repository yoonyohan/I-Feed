package com.example.ifeed.application

import android.content.Context
import com.example.ifeed.data.FeedDataBase
import com.example.ifeed.data.OfflineRepository
import com.example.ifeed.data.Repository

class FeedAppContainer(
    private val context: Context
): AppContainer {
    override val offlineRepository: Repository by lazy {
        OfflineRepository(
            userDao = FeedDataBase.getDataBase(context).userDao(),
            postDao = FeedDataBase.getDataBase(context).postDao()
        )
    }
}