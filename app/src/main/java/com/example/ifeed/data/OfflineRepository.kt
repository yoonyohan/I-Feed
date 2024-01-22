package com.example.ifeed.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineRepository(
    private val userDao: UserDao,
    private val postDao: PostDao
): Repository {
    override suspend fun addNewUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.addNewUser(user)
        }
    }

    override suspend fun getUserByName(userName: String): UserNameAndPassword? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByName(userName)
        }
    }

    override suspend fun loggedIn(userId: Int, isLoggedIn: Boolean) {
        withContext(Dispatchers.IO) {
            Log.d("OfflineRepository", " Offline Repository - The user id : $userId and is logged in $isLoggedIn") // Good
            userDao.loggedIn(userId, isLoggedIn)
        }
    }

    override suspend fun getUserById(userId: Int): LoggedInData? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }

    override suspend fun addNewPost(post: Post) {
        withContext(Dispatchers.IO) {
            postDao.addNewPost(post)
        }
    }

    override suspend fun getAllPostByUserId(userId: Int): Flow<List<Post>> {
        return withContext(Dispatchers.IO) {
            postDao.getAllPostByUserId(userId)
        }
    }

    override suspend fun getAllPost(): Flow<List<Post>> {
        return withContext(Dispatchers.IO) {
            postDao.getAllPosts()
        }
    }

    override suspend fun deletePost(postId: Int) {
        withContext(Dispatchers.IO) {
            postDao.deletePost(postId)
        }
    }
}