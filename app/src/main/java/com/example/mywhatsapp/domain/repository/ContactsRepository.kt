package com.example.mywhatsapp.domain.repository

import com.example.mywhatsapp.domain.model.ModelChat
import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getAllUsers(deviceContacts: List<String>): Flow<Resource<List<User>>>
    fun getAllChats(userId: String) : Flow<Resource<List<ModelChat>>>
}