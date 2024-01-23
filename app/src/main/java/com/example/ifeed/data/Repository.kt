package com.example.ifeed.data

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllPosts(): Flow<List<PostFire>>
}