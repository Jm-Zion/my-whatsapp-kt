package com.example.mywhatsapp.domain.repository

import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.presentation.MainActivity
import com.example.mywhatsapp.util.Resource
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun phoneNumberSignIn(phoneNumber: String, activity: MainActivity): Flow<Resource<Boolean>>
    fun isUserAuthenticated(): Boolean
    fun getUserId() : String
    suspend fun signInWithAuthCredential(phoneAuthCredential: PhoneAuthCredential): Resource<Boolean>
    fun createUserProfile(user: User, userId: String): Flow<Resource<Boolean>>
}