package com.example.mywhatsapp.presentation.Chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywhatsapp.domain.model.ModelChat
import com.example.mywhatsapp.domain.use_case.AuthenticationUseCase
import com.example.mywhatsapp.domain.use_case.ChatUseCase
import com.example.mywhatsapp.presentation.IViewStatusHandling
import com.example.mywhatsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {
    private var _chatList = MutableStateFlow<List<ModelChat>>(emptyList())
    val chatList: StateFlow<List<ModelChat>> = _chatList

    private lateinit var iChatView : IViewStatusHandling

    fun fetchAllChats(listener: IViewStatusHandling) = viewModelScope.launch {
        iChatView = listener
        chatUseCase.getAllChats(authenticationUseCase.getUserId()).collectLatest {
            when(it) {
                is Resource.Loading -> {
                    iChatView.showProgressBar()
                }

                is Resource.Success -> {
                    iChatView.hideProgressBar()
                }

                is Resource.Error -> {
                    iChatView.showError()
                }
            }
        }
    }

}