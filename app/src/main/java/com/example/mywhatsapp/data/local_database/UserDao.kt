package com.example.mywhatsapp.data.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mywhatsapp.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE userID = :userId")
    fun getUserByID(userId: String): Flow<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}