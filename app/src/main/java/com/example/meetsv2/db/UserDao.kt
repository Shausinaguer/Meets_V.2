package com.example.meetsv2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE email = :email AND senha = :senha LIMIT 1")
    suspend fun findByEmailAndSenha(email: String, senha: String): User?
}
