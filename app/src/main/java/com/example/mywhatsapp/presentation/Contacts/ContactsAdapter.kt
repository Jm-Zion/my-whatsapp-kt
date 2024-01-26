package com.example.mywhatsapp.presentation.Contacts

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
import com.example.mywhatsapp.domain.model.User
import com.example.mywhatsapp.util.OutlineProvider

class ContactsAdapter : ListAdapter<User, ContactsAdapter.ViewHolder>(ContactDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactsAdapter = getItem(position)
        holder.bindView(contactsAdapter)
    }

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        fun bindView(user: User){
            val userName: TextView = view.findViewById(R.id.txtUserName)
            val userStatus: TextView = view.findViewById(R.id.txtUserStatus)
            val userProfile : ImageView = view.findViewById(R.id.userImage)
            userProfile.outlineProvider = OutlineProvider()
            userProfile.clipToOutline = true
            userName.text = user.userName
            userStatus.text = user.userStatus
            Glide.with(itemView.context).load(user.userImage).into(userProfile)
        }
    }

    class ContactDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}