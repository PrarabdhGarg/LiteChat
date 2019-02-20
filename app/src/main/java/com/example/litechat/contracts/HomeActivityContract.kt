package com.example.litechat.contracts

import android.content.ContentResolver
import android.content.Context
import com.example.litechat.model.contactsRoom.User


interface HomeActivityContract {

    interface Presenter{

        fun getContacts()
        fun getUserDataOnLogin(number : String)
        fun getUsers()
        fun passUserList(): List<User>

    }

    interface Model{

        fun roomGetData(applicationContext: Context): List<User>
        fun getUserDataFromFirestore(number : String)
        fun roomSetData(applicationContext: Context, userList: List<User>)

    }

    interface View{

        fun passContentResolver() : ContentResolver
        fun passContextRoom(): Context

    }

}