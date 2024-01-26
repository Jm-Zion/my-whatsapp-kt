package com.example.mywhatsapp.data.repository

import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.presentation.MainActivity
import com.example.mywhatsapp.domain.repository.AuthRepository
import com.example.mywhatsapp.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.security.auth.callback.Callback
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(var firebaseAuth: FirebaseAuth, var firebaseStore: FirebaseFirestore) : AuthRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun createUserProfile(user: User, userId: String): Flow<Resource<Boolean>> = callbackFlow {
        try {
            user.userId = userId
            trySend(Resource.Loading);
            firebaseStore.collection("users").document(user.userId).set(user).addOnSuccessListener {
                trySend(Resource.Success<Boolean>(true))
            }
            .addOnFailureListener {
                trySend(Resource.Error("User profile creation failed due to ${it.localizedMessage?: ""}"))
            }
            awaitClose()
        }
        catch(exception: Exception){
            trySend(Resource.Error("User profile creation failed due to ${exception.localizedMessage ?: ""}"))
        }
    }

    override fun phoneNumberSignIn(
        phoneNumber: String,
        activity: MainActivity
    ): Flow<Resource<Boolean>> = channelFlow {
        try {
            trySend(Resource.Loading).isSuccess
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setActivity(activity)
                .setTimeout(60, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        coroutineScope.launch {
                            signInWithAuthCredential(p0)
                        }
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        coroutineScope.launch {
                            trySend(
                                Resource.Error(
                                    p0.localizedMessage ?: "An Error Occurred"
                                )
                            ).isSuccess
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        coroutineScope.launch {
                            withContext(Dispatchers.Main) {
                                activity.showBottomSheet()
                            }

                            activity.otpValue.collect {
                                if (it.isNotBlank()) {
                                    trySend(
                                        (signInWithAuthCredential(
                                            PhoneAuthProvider.getCredential(
                                                verificationId,
                                                it
                                            )
                                        ))
                                    ).isSuccess
                                }
                            }
                        }
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            awaitClose()
        } catch (exception: Exception) {
            Resource.Error(exception.localizedMessage ?: "An Error Occured")
        }
    }


    override suspend fun signInWithAuthCredential(phoneAuthCredential: PhoneAuthCredential): Resource<Boolean> =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener {
                    continuation.resume(Resource.Success(true))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(
                        Resource.Error(
                            exception.localizedMessage ?: "An Error Occured"
                        )
                    )
                }
        }

    override fun getUserId(): String {
        return firebaseAuth.currentUser?.let {
            it.uid
        } ?: ""
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null;
    }
}