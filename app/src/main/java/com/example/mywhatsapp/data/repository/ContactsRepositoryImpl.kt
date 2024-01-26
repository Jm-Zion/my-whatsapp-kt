package com.example.mywhatsapp.data.repository

import com.example.mywhatsapp.data.local_database.UserDao
import com.example.mywhatsapp.domain.model.ModelChat
import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.domain.repository.ContactsRepository
import com.example.mywhatsapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore, protected val userDao: UserDao) : ContactsRepository {

    var whatsAppContacts : MutableList<User> = arrayListOf()

    override fun getAllUsers(deviceContacts: List<String>): Flow<Resource<List<User>>> = channelFlow{
        try {
            trySend(Resource.Loading)
            val contacts = userDao.getAllUsers().firstOrNull()
            if(contacts.isNullOrEmpty()) {
                for (contact in deviceContacts) {
                    val query = firestore.collection("users").whereEqualTo("userNumber", contact)
                    query.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                val user = getUserFromDocument(document)
                                userDao.insertUser(user)
                                CoroutineScope(Dispatchers.IO).launch {
                                    userDao.getAllUsers().collectLatest {
                                        trySend(Resource.Success(it))
                                    }
                                }
                                trySend(Resource.Success(whatsAppContacts))
                            }
                        }
                    }
                }
            }
            else {
                trySend(Resource.Success(contacts))
            }
            awaitClose()
        }
        catch(err: Exception){
            trySend(Resource.Error(err.message))
        }
    }

    fun getUserFromDocument(document: QueryDocumentSnapshot) : User {
        return User(
            userName= document.getString("userName") ?: "",
            userNumber = document.getString("userNumber")?: "",
            userId = document.getString("id")?: "",
            userImage = document.getString("userImage")?: "",
            userStatus = document.getString("userStatus")?: ""
        )
    }

    override fun getAllChats(userId: String): Flow<Resource<List<ModelChat>>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            var query = firestore.collection("chats").whereArrayContains("chatParticipants", userId)
            var listener = query.addSnapshotListener { snapshot, exception ->
                if(exception != null) {
                    trySend(Resource.Error(exception.localizedMessage?: "An Error Occurred"))
                    return@addSnapshotListener
                }
                snapshot?.let { documents ->
                    val chats: MutableList<ModelChat> = mutableListOf<ModelChat>()
                    for(document in documents){
                        val chat = getChatFormDocument(document)
                        chats.add(chat)
                    }
                    trySend(Resource.Success(chats))
                }
            }
            awaitClose {
                listener.remove()
            }
        }
        catch(e: Exception){
            trySend(Resource.Error(e.localizedMessage?: "An Error Occurred"))
        }
    }

    private fun getChatFormDocument(document: QueryDocumentSnapshot): ModelChat {
        return ModelChat(
            chatId= document.id,
            chatName = document.getString("chatName").toString(),
            chatImage = document.getString("chatImage").toString(),
            chatLastMessageTimeStamp = document.getString("chatLastMessageTimeStamp").toString(),
            chatLastMessage = document.getString("chatLastMessage").toString(),
            chatParticipants = document.get("chatParticipants") as List<String>
        )
    }
}