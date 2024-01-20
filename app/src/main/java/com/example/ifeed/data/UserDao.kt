package com.example.ifeed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class UserNameAndPassword(
    val userName: String,
    val password: String
)

data class LoggedInData(
    val id: Int,
    val userName: String,
    val isLoggedIn: Boolean
)

@Dao
interface UserDao {
    @Insert
    suspend fun addNewUser(user: User)

    @Query("SELECT userName, password FROM user_table WHERE userName = :userName")
    suspend fun getUserByName(userName: String): UserNameAndPassword?

    @Query("UPDATE user_table SET isLoggedIn = :isLoggedIn WHERE id = :userId")
    suspend fun loggedIn(userId: Int, isLoggedIn: Boolean)

    @Query("SELECT id, userName, isLoggedIn FROM user_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): LoggedInData?
}