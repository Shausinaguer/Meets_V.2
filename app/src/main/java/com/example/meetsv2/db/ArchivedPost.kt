package com.example.meetsv2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archived_posts")
data class ArchivedPost(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val content: String,
    val timestamp: Long,
    val userId: Int
)
