package com.example.litechat.contracts

import android.content.ContentResolver
import android.content.Context
import android.support.v4.app.Fragment
import com.example.litechat.model.contactsRoom.User
import com.example.litechat.view.fragments.FragmentChat
import com.example.litechat.presenter.HomeActivityPresenter
import com.example.litechat.presenter.StatusFragmentPresenter


interface HomeActivityContract {
  
    interface Model{
        fun retrievePersonalChatDataFromFirestore(presenter: HomeActivityContract.Presenter)
        fun getUserDataFromFirestore(number : String)
        fun getCurrentActivitiesOfOtherUsers(presenter : StatusFragmentPresenter)
      
    }

    interface Presenter{

        fun getUserDataOnLogin(number : String)
        fun getPersonalChatsFromFirestore()
        fun sortPersonalChatList()
    }

    interface View{

        fun passContext(): Context
        fun  getPersonalChats()
        fun isChatFragmentActive(): Boolean
        fun getInstanceOfFragmentChat() : FragmentChat

    }

}