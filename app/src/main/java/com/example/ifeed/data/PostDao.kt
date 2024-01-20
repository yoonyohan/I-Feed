package com.example.ifeed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewPost(post: Post)

    @Query("SELECT * FROM post_table WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllPostByUserId(userId: Int): Flow<List<Post>>

    @Query("SELECT * FROM post_table ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("DELETE FROM post_table WHERE postId = :postId")
    suspend fun deletePost(postId: Int)
}