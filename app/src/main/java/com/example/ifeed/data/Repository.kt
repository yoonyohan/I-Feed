package com.example.ifeed.data

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun addNewUser(user: User)
    suspend fun getUserByName(userName: String): UserNameAndPassword?
    suspend fun loggedIn(userId: Int, isLoggedIn: Boolean)
    suspend fun getUserById(userId: Int): LoggedInData?


    suspend fun addNewPost(post: Post)
    suspend fun getAllPostByUserId(userId: Int): Flow<List<Post>>
    suspend fun getAllPost(): Flow<List<Post>>
    suspend fun deletePost(postId: Int)
}