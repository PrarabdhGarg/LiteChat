package com.example.litechat.contracts

import android.content.Context
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.contactsRoom.User

interface ContactFragContract {

    interface Model{

        fun getFirebaseData()
        fun roomGetData(applicationContext: Context): List<User>
        fun roomSetData(applicationContext: Context, userList: List<User>)
    }


    interface Presenter{

        fun getContacts()
        fun getUsers()
        fun passUserList(): List<User>
    }

    interface View{



    }
}