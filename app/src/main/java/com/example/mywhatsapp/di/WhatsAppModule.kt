package com.example.mywhatsapp.di

import android.content.Context
import androidx.room.Room
import com.example.mywhatsapp.data.local_database.UserDao
import com.example.mywhatsapp.data.local_database.UserDatabase
import com.example.mywhatsapp.data.repository.AuthRepositoryImpl
import com.example.mywhatsapp.data.repository.ContactsRepositoryImpl
import com.example.mywhatsapp.domain.repository.AuthRepository
import com.example.mywhatsapp.domain.repository.ContactsRepository
import com.example.mywhatsapp.domain.use_case.AuthenticationUseCase
import com.example.mywhatsapp.domain.use_case.ChatUseCase
import com.example.mywhatsapp.domain.use_case.ContactUseCase
import com.example.mywhatsapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WhatsAppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, Constants.userRoomDatabse).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase) : UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideContactUseCase(repository: ContactsRepository) : ContactUseCase {
        return ContactUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideChatUseCase(repository: ContactsRepository) : ChatUseCase {
        return ChatUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCase(authRepository: AuthRepository): AuthenticationUseCase {
        return AuthenticationUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideContactRepository(firestore : FirebaseFirestore,userDao: UserDao,) : ContactsRepository {
        return ContactsRepositoryImpl(firestore, userDao)
    }
}
