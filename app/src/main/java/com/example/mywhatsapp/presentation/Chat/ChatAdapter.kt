package com.example.mywhatsapp.presentation.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mywhatsapp.R
import com.example.mywhatsapp.domain.model.ModelChat
import com.example.mywhatsapp.util.OutlineProvider

class ChatAdapter: ListAdapter<ModelChat, ChatAdapter.ChatViewHolder>(ChatDiffUtil()) {

    class ChatDiffUtil : DiffUtil.ItemCallback<ModelChat>(){
        override fun areContentsTheSame(oldItem: ModelChat, newItem: ModelChat): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ModelChat, newItem: ModelChat): Boolean {
            return oldItem.chatId == newItem.chatId
        }
    }

    class ChatViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        fun bindView(model: ModelChat){
            var chatImageView : ImageView = view.findViewById(R.id.chatImage)
            var chatName: TextView = view.findViewById(R.id.chatName)
            var chatLastMessageView: TextView = view.findViewById(R.id.chatLastMessage)
            var chatLastMessageTimeStamView: TextView = view.findViewById(R.id.chatLastMessageTimestamp)
            chatName.text = model.chatName
            chatLastMessageView.text = model.chatLastMessage
            chatLastMessageTimeStamView.text = model.chatLastMessageTimeStamp
            chatImageView.outlineProvider = OutlineProvider()
            chatImageView.clipToOutline = true
            Glide.with(itemView.context).load(model.chatImage).into(chatImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.chat_card, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val modelChat = getItem(position)
        holder.bindView(modelChat)
    }
}