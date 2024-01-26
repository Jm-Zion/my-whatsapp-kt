package com.example.mywhatsapp.presentation

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.domain.use_case.AuthenticationUseCase
import com.example.mywhatsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authUseCase : AuthenticationUseCase
) : ViewModel(){
    private lateinit var authProgressStatus : IAuthProgressStatus

    fun setIAuthProgressStatus(authProgressView: IAuthProgressStatus){
        this.authProgressStatus = authProgressView
    }

    fun signInWithPhoneNumber(phoneNumber: String, activity: MainActivity): Job {
        return viewModelScope.launch {
            authUseCase.phoneNumberSignIn(phoneNumber, activity ).collect{
                when(it) {
                    Resource.Loading -> authProgressStatus.showProgressBar()
                    is Resource.Error -> authProgressStatus.showError()
                    is Resource.Success -> {
                        authProgressStatus.hideProgressBar()
                        authProgressStatus.changeViewVisibilty()
                        authProgressStatus.dismissOtpFragmentBottomSheetDialog()
                    }
                }
            }
        }
    }

    fun createUserProfile(user: User) {
        viewModelScope.launch {
            authUseCase.createUserProfile(user, authUseCase.getUserId()).collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        authProgressStatus.showProgressBar()
                    }

                    is Resource.Success -> {
                        authProgressStatus.hideProgressBar()
                        authProgressStatus.changeViewVisibilty()
                    }

                    is Resource.Error -> {
                        authProgressStatus.showError()
                    }
                }
            }
        }
    }
}