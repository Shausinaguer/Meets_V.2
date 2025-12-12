package com.example.meetsv2.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Se um usuário for deletado, seus posts também serão.
        )
    ]
)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int, // O ID do usuário que criou o post
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
