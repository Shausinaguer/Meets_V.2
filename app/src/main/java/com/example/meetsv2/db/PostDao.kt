package com.example.meetsv2.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Insert
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Transaction // Garante que a busca do post e do usuário seja atômica
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getPostsWithAuthors(): Flow<List<PostWithAuthor>>

    @Transaction
    @Query("SELECT * FROM posts WHERE content LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchPostsWithAuthors(searchQuery: String): Flow<List<PostWithAuthor>>
}
