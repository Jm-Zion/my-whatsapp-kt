package com.example.mywhatsapp.domain.use_case

import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.presentation.MainActivity
import com.example.mywhatsapp.domain.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val  authRepository: AuthRepository
) {
    fun phoneNumberSignIn(phoneNumber : String, activity: MainActivity)= authRepository.phoneNumberSignIn(phoneNumber, activity)

    fun isUserAuthenticated() = authRepository.isUserAuthenticated()

    fun getUserId() = authRepository.getUserId()

    suspend fun signInWithAuthCredential(phoneCredentals: PhoneAuthCredential) = authRepository.signInWithAuthCredential(phoneCredentals)

    fun createUserProfile(user : User, userId: String) = authRepository.createUserProfile(user, userId)
}