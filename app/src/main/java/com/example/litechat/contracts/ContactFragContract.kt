package com.example.litechat.contracts

import android.content.Context
import com.example.litechat.listeners.BoomListener
import com.example.litechat.listeners.CallListenerObject
import com.example.litechat.model.ChatObject
import com.example.litechat.model.contactsRoom.User
import java.util.ArrayList

interface ContactFragContract {

    interface Model{

        fun getFirebaseData()
        fun roomGetData(applicationContext: Context): List<User>
        fun roomSetData(applicationContext: Context, userList: List<User>)
        fun roomDeleteData(applicationContext: Context)
        fun startChatActivity(number:String,presenter: ContactFragContract.Presenter)
        fun roomGetName(applicationContext: Context , number: String) : String
    }


    interface Presenter{

        fun getContacts(adapterListener: BoomListener)
        fun getUsers(adapterListener: BoomListener)
        fun compareUserContact(adapterListener: BoomListener)
        fun passUserList(): List<User>
        fun startNewChatFromContact(number:String)
        fun passDataForChatActivity(chatObject: ChatObject)
    }

    interface View{
        fun startChatActivity(chatObject: ChatObject)
    }

}