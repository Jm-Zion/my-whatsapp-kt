package com.example.mywhatsapp.domain.model

data class ModelChat (
    var chatId: String,
    var chatName: String,
    var chatImage: String,
    var chatParticipants: List<String>,
    var chatLastMessage: String,
    var chatLastMessageTimeStamp: String,
)