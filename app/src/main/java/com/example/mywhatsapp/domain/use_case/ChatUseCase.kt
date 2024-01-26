package com.example.mywhatsapp.domain.use_case

import com.example.mywhatsapp.domain.repository.ContactsRepository
import javax.inject.Inject

class ChatUseCase @Inject constructor(private val userRepository: ContactsRepository) {
    fun getAllChats(userId: String) = userRepository.getAllChats(userId)
}