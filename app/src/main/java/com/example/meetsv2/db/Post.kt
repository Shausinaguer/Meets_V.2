package com.example.meetsv2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
