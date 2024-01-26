package com.example.ifeed.data

import com.example.ifeed.business.FeedPost
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllPosts(): Flow<List<PostFire>>

    fun readAllPosts(completion: (List<FeedPost>) -> Unit)
    fun getAllUsersForMsg(): Flow<List<UserMsg>>
}