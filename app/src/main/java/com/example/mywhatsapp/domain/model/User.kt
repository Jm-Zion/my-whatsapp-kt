package com.example.mywhatsapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "users")
data class User(
    @PrimaryKey
    var userId : String = "",
    var userName: String = "",
    var userImage : String = "",
    var userNumber: String = "",
    var userStatus: String = ""
)
