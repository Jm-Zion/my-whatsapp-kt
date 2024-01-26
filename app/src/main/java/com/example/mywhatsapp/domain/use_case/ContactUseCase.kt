package com.example.mywhatsapp.domain.use_case

import com.example.mywhatsapp.domain.repository.ContactsRepository
import javax.inject.Inject

class ContactUseCase @Inject constructor(private var contactsRepository: ContactsRepository){

    fun getAllUsers(devicesContacts : List<String>) = contactsRepository.getAllUsers(devicesContacts)

}