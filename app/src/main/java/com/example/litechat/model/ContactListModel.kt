package com.example.litechat.model

import android.arch.persistence.room.Room
import android.content.Context
import com.example.litechat.contracts.ContactFragContract
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.model.contactsRoom.User

class ContactListModel: ContactFragContract.Model{

    override fun roomSetData(applicationContext: Context, userList: List<User>) {
        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()

        for(item in userList){

            db.userDao().insertContact(item)
        }

    }

    override fun roomGetData(applicationContext: Context): List<User> {

        val db = Room.databaseBuilder(applicationContext, AppDatabse::class.java, "Contact_Database")
            .allowMainThreadQueries().build()

        return  db.userDao().getContactList()

    }


    override fun getFirebaseData() {

    }
}