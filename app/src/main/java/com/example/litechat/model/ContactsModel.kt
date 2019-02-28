package com.example.litechat.model

import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.model.contactsRoom.AppDatabse
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.presenter.StatusFragmentPresenter

class ContactsModel: HomeActivityContract.Model{
    override fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserDataFromFirestore(number: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}