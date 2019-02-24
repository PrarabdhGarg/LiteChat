package com.example.litechat.contracts

import android.content.ContentResolver
import android.content.Context
import android.support.v4.app.Fragment
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.view.fragments.FragmentChat


interface HomeActivityContract {



    interface Model{

        fun roomGetData(applicationContext: Context): List<User>
        fun getUserDataFromFirestore(number : String)
        fun roomSetData(applicationContext: Context, userList: List<User>)
        fun retrievePersonalChatDataFromFirestore(presenter :HomeActivityContract.Presenter)
    }

    interface Presenter{

        fun getContacts()
        fun getUserDataOnLogin(number : String)
        fun getUsers()
        fun passUserList(): List<User>
        fun getPersonalChatsFromFirestore()
        fun sortPersonalChatList()

    }

    interface View{

        fun passContentResolver() : ContentResolver
        fun passContext(): Context
        fun  getPersonalChats()
        fun isChatFragmentActive(): Boolean
        fun getInstanceOfFragmentChat() : FragmentChat

    }

}