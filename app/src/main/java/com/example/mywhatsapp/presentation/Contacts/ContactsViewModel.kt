package com.example.mywhatsapp.presentation.Contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.domain.use_case.ContactUseCase
import com.example.mywhatsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private var contactUseCase: ContactUseCase
): ViewModel(){

    private var _whatsAppContactsList = MutableStateFlow<List<User>>(emptyList())
    val whatsAppContactsList : StateFlow<List<User>> = _whatsAppContactsList

    private lateinit var contactView : IContactsView;

    fun getAllWhatsAppContacts(deviceContacts: List<String>, interfaceListener: IContactsView) = viewModelScope.launch {
        contactView = interfaceListener
        contactUseCase.getAllUsers(deviceContacts).collectLatest {
            when(it) {
                is Resource.Success -> {
                    _whatsAppContactsList.value = it.data
                    contactView.hideProgressBar()
                }
                is Resource.Error -> {
                    contactView.showError()
                }
                is Resource.Loading -> {
                    contactView.showProgressBar()
                }
            }
        }
    }
}