package com.example.mywhatsapp.presentation.Contacts

import android.content.Context
import android.provider.ContactsContract
import org.jetbrains.annotations.Contract

class ContactsManager(private val context: Context) {

    private val contacts = arrayListOf<String>()

    fun getContacts() : ArrayList<String> {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let{
            while(it.moveToNext()){
                val value = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var phoneNumber = ""
                if(value > -1) {
                    phoneNumber = it.getString(value)
                    contacts.add(phoneNumber)
                }
            }
        }
        return contacts
    }
}