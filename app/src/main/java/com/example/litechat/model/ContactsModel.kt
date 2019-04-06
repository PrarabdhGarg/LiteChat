package com.example.litechat.model

import android.content.Context
import com.example.litechat.contracts.HomeActivityContract
import com.example.litechat.presenter.StatusFragmentPresenter

class ContactsModel: HomeActivityContract.Model{
    override fun retrieveURLFromRoom(presenter: HomeActivityContract.Presenter, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun getCurrentActivitiesOfOtherUsers(presenter: StatusFragmentPresenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserDataFromFirestore(number: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}