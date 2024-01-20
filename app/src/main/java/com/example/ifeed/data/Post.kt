package com.example.ifeed.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "post_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val postId: Int = 0,
    val title: String,
    val description: String,
    val topic: String,
    val edited: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val userId: Int,
    val likeCount: Int = 0
)
