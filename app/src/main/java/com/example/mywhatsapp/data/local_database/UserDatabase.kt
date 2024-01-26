package com.example.mywhatsapp.data.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mywhatsapp.domain.model.User

@Database(entities = [User::class], version=1)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao;
}